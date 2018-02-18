package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scape.scape2d.engine.motion.collision._
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.motion._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.integral.LinearMotionIntegral
import scape.scape2d.engine.core.integral.RotationIntegral
import scape.scape2d.engine.core.matter.Body
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.motion.collision.detection.rotation.BruteForceBasedRotationalCollisionDetector
import scape.scape2d.engine.motion.collision.detection.rotation.IterativeRootFindingRotationalCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.BruteForceLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.core.integral.MotionIntegral

class Nature(
  var timescale:Timescale = Timescale(Frequency(60, Second)),
  val motionIntegral:MotionIntegral = MotionIntegral()
) extends Actor {
  private val log = Logger.getLogger(getClass);
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def add(timeDependent:TimeDependent):Unit = this ! (() => temporals += new Temporal(timeDependent));
  
  def add(particle:Particle):Unit = this ! (() => particles += particle);
  
  def add(bond:Bond):Unit = {
    add(bond.particles._1);
    add(bond.particles._2);
  };
  
  def add(body:Body):Unit = body.movables.foreach(add);
  
  override def act = {
    log.info("Nature has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      val timescale = this.timescale;
      val integrationMillis = timescale.integrationFrequency.occurenceDuration.milliseconds;
      val timestepMillis = timescale.timestep.milliseconds;
      
      motionIntegral.integrate(particles, timestepMillis);
      temporals.foreach(_.integrate(timestepMillis));
      temporals = temporals.filter(_.timeleft > 0);
      dispatchInputs();
      
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = (integrationMillis - cycleMillis).toLong;
      log.info("Cycle finished! Took %d/%f ms".format(cycleMillis, integrationMillis));
      if(cooldown > 0) Thread.sleep(cooldown);
    }
  }
  
  private def dispatchInputs():Unit = receiveWithin(0) {
    case act:(() => Unit) =>
      act();
      dispatchInputs();
    case TIMEOUT => return;
    case unknown =>
      log.warn("Unknown input " + unknown);
      dispatchInputs();
  }
}