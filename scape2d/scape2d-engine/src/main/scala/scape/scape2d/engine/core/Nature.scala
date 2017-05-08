package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer

import org.apache.log4j.Logger

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.motion.Movable
import scape.scape2d.engine.motion.integrateMotion

class Nature(fps:Double) extends Actor {
  private val log = Logger.getLogger(getClass);
  private val integrations = new ArrayBuffer[Long => Unit];
  private var timescale = scaleTime(1, 1);
  
  def add(timeSubject:TimeDependent) = integrations += timeSubject.integrate _;
  
  def add(movable:Movable) = integrations += (integrateMotion(movable, _:Long));
  
  def add(particle:Particle) = {
    integrations += particle.integrateForces _;
    integrations += (integrateMotion(particle, _:Long));
  }
  
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
  
  private def integrate(timestep:Long) = {
    log.debug("Time integration phase starts...");
    integrations.foreach(_(timestep));
    log.debug("Time integration phase ended.");
  }
  
  private def dispatchInputs() = {
    log.debug("Input dispatching phase starts...");
    var endOfMailbox = false;
    while(!endOfMailbox) {
      receiveWithin(0) {
        case ExertForce(p, f) => p.forces += f;
        case ScaleTime(fm, tm) => timescale = scaleTime(fm, tm);
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
    log.debug("Input dispatching phase ended.");
  }
}