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

class Nature(
  var timescale:Timescale = Timescale(Frequency(60, Second)),
  val collisionDetector:CollisionDetector[Particle] = new BruteForceBasedCollisionDetector(detectWithDiscriminant))
extends Actor {
  private val log = Logger.getLogger(getClass);
  private val linearMotionIntegral = LinearMotionIntegral(collisionDetector);
  private val rotationIntegral = RotationIntegral();
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def add(timeDependent:TimeDependent):Unit = this ! (() => temporals += new Temporal(timeDependent));
  
  def add(particle:Particle):Unit = this ! (() => particles += particle);
  
  def add(bond:Bond):Unit = this ! (() => {
    add(bond.particles._1);
    add(bond.particles._2);
  });
  
  def add(body:Body):Unit = this ! (() => body.movables.foreach(add));
  
  override def act = {
    log.info("Nature has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      val timescale = this.timescale;
      val integrationMillis = timescale.integrationFrequency.occurenceDuration.milliseconds;
      val timestepMillis = timescale.timestep.milliseconds;
      
      linearMotionIntegral.integrate(particles, timestepMillis);
      rotationIntegral.integrate(particles, timestepMillis);
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