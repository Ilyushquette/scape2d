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
import scape.scape2d.engine.core.integral.LinearMotionIntegral
import scape.scape2d.engine.core.input.AddBody
import scape.scape2d.engine.core.matter.Body

class Nature(val collisionDetector:CollisionDetector[Particle], fps:Double) extends Actor {
  private val log = Logger.getLogger(getClass);
  private val linearMotionIntegral = LinearMotionIntegral(collisionDetector);
  private var timeSubjects = Set[TimeDependent]();
  private var particles = Set[Particle]();
  private var timescale = scaleTime(1, 1);
  
  def this(fps:Double) = this(new BruteForceBasedCollisionDetector(detectWithDiscriminant), fps);
  
  def add(timeSubject:TimeDependent) = this ! AddTimeSubject(timeSubject);
  
  def add(particle:Particle) = this ! AddParticle(particle);
  
  def add(bond:Bond) = this ! AddBond(bond);
  
  def add(body:Body) = this ! AddBody(body);
  
  private def scaleTime(fm:Double, tm:Double) = 1000 / (fps * fm) <-> 1000 / fps * tm;
  
  implicit def toTimescaleBuilder(frequency:Double):TimescaleBuilder = new TimescaleBuilder(frequency);
  
  override def act = {
    log.info("Nature has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      linearMotionIntegral.integrate(particles, timescale.timestep);
      timeSubjects = timeSubjects.filter(_.integrate(timescale.timestep));
      dispatchInputs();
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = timescale.frequency - cycleMillis;
      log.info("Cycle finished! Took %d/%d ms".format(cycleMillis, timescale.frequency));
      Thread.sleep(if (cooldown > 0) cooldown else 0);
    }
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
        case AddBody(body) =>
          body.bonds.foreach(add);
          body.movables.foreach(add);
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
  }
}