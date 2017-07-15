package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scape.scape2d.engine.motion.collision._
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.motion._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.input.ScaleTime
import scape.scape2d.engine.core.input.AddTimeSubject
import scape.scape2d.engine.core.input.AddParticle
import scape.scape2d.engine.core.input.AddBond
import scape.scape2d.engine.core.matter.Bond

class Nature(val collisionDetector:CollisionDetector[Particle], fps:Double) extends Actor {
  private val log = Logger.getLogger(getClass);
  private var timeSubjects = Set[TimeDependent]();
  private var particles = Set[Particle]();
  private var timescale = scaleTime(1, 1);
  
  def this(fps:Double) = this(new BruteForceBasedCollisionDetector(detectWithDiscriminant), fps);
  
  def add(timeSubject:TimeDependent) = this ! AddTimeSubject(timeSubject);
  
  def add(particle:Particle) = this ! AddParticle(particle);
  
  def add(bond:Bond) = this ! AddBond(bond);
  
  private def scaleTime(fm:Double, tm:Double) = 1000 / (fps * fm) <-> 1000 / fps * tm;
  
  implicit def toTimescaleBuilder(frequency:Double):TimescaleBuilder = new TimescaleBuilder(frequency);
  
  override def act = {
    log.info("Nature has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      integrate(timescale.timestep);
      dispatchInputs();
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = timescale.frequency - cycleMillis;
      log.info("Cycle finished! Took %d/%d ms".format(cycleMillis, timescale.frequency));
      Thread.sleep(if (cooldown > 0) cooldown else 0);
    }
  }
  
  private def integrate(timestep:Double):Unit = {
    particles.foreach(integrateAcceleration(_));
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val integratedTime = handleCollisionAndIntegrate(earliestCollision);
      val remainingTime = timestep - integratedTime;
      if(remainingTime > 0) integrate(remainingTime);
    }else integrateMotionAndSubjects(timestep);
  }
  
  private def handleCollisionAndIntegrate(collision:Collision[Particle]) = {
    val particle1 = collision.concurrentPair._1;
    val particle2 = collision.concurrentPair._2;
    val safeTime = findSafeTime(collision, 0.005);
    val forces = resolveForces(collision);
    if(safeTime > 0) integrateMotionAndSubjects(safeTime);
    particle1.setForces(particle1.forces :+ forces._1);
    particle2.setForces(particle2.forces :+ forces._2);
    safeTime;
  }
  
  private def integrateMotionAndSubjects(timestep:Double) = {
    particles.foreach(integrateMotion(_, timestep));
    val validTimeSubjects = timeSubjects.filter(_.integrate(timestep));
    timeSubjects = validTimeSubjects;
  }
  
  private def dispatchInputs() = {
    var endOfMailbox = false;
    while(!endOfMailbox) {
      receiveWithin(0) {
        case ScaleTime(fm, tm) => timescale = scaleTime(fm, tm);
        case AddTimeSubject(ts) => timeSubjects = timeSubjects + ts;
        case AddParticle(p) => particles = particles + p;
        case AddBond(bond) => 
          bond.particles._1.setBonds(bond.particles._1.bonds + bond);
          bond.particles._2.setBonds(bond.particles._2.bonds + bond.reversed);
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
  }
}