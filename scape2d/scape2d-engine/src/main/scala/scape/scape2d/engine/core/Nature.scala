package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer

import org.apache.log4j.Logger

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.motion.Movable
import scape.scape2d.engine.motion.integrateMotion

class Nature(val fps:Integer) extends Actor {
  private val log = Logger.getLogger(getClass);
  private val integrations = new ArrayBuffer[Long => Unit];
  
  def add(timeSubject:TimeDependent) = integrations += timeSubject.integrate _;
  
  def add(movable:Movable) = integrations += (integrateMotion(movable, _:Long));
  
  def add(particle:Particle) = {
    integrations += particle.integrateForces _;
    integrations += (integrateMotion(particle, _:Long));
  }
  
  override def act = {
    val timestep = 1000 / fps;
    log.info("Nature has been started with timestep " + timestep);
    loop {
      val cycleStart = System.currentTimeMillis;
      integrate(timestep);
      dispatchInputs();
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = timestep - cycleMillis;
      log.debug("Cycle finished! Took %d/%d ms".format(cycleMillis, timestep));
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
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
    log.debug("Input dispatching phase ended.");
  }
}