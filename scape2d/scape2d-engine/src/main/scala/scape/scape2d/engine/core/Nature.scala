package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scape.scape2d.engine.motion.collision._
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.motion._
import scape.scape2d.engine.core.matter.Particle

class Nature(val collisionDetector:CollisionDetector[Particle], fps:Double) extends Actor {
  private val log = Logger.getLogger(getClass);
  private var timeSubjects = Set[TimeDependent]();
  private var particles = Set[Particle]();
  private var timescale = scaleTime(1, 1);
  
  def this(fps:Double) = this(new BruteForceBasedCollisionDetector(detectWithDiscriminant), fps);
  
  def add(timeSubject:TimeDependent) = this ! AddTimeSubject(timeSubject);
  
  def add(particle:Particle) = this ! AddParticle(particle);
  
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
      log.debug("Cycle finished! Took %d/%d ms".format(cycleMillis, timescale.frequency));
      Thread.sleep(if (cooldown > 0) cooldown else 0);
    }
  }
  
  private def integrate(timestep:Double):Unit = {
    log.debug("Time integration phase starts...");
    particles.foreach(integrateAcceleration(_));
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      log.debug("Earliest COLLISION DETECTED! Time left: " + earliestCollision.time);
      val integratedTime = handleCollisionAndIntegrate(earliestCollision);
      val remainingTime = timestep - integratedTime;
      if(remainingTime > 0) integrate(remainingTime);
    }else integrateMotionAndSubjects(timestep);
    log.debug("Time integration phase ended.");
  }
  
  private def handleCollisionAndIntegrate(collision:Collision[Particle]) = {
    val particle1 = collision.concurrentPair._1;
    val particle2 = collision.concurrentPair._2;
    val safeTime = findSafeTime(collision, 0.005);
    log.debug(safeTime + " time safe to be integrated to prevent intersection");
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
    log.debug("Input dispatching phase starts...");
    var endOfMailbox = false;
    while(!endOfMailbox) {
      receiveWithin(0) {
        case ScaleTime(fm, tm) => timescale = scaleTime(fm, tm);
        case AddTimeSubject(ts) => timeSubjects = timeSubjects + ts;
        case AddParticle(p) => particles = particles + p;
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
    log.debug("Input dispatching phase ended.");
  }
}