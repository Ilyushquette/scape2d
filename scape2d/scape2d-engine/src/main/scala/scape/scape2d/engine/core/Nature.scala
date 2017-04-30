package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT;
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedHashSet
import org.apache.log4j.Logger
import scape.scape2d.engine.matter.Event
import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.motion._;
import scala.collection.mutable.ArrayBuffer
import scape.scape2d.engine.geom.Vector2D

class Nature(val fps:Integer) extends Actor {
  private val log = Logger.getLogger(getClass);
  private val particles = new LinkedHashSet[Particle];
  private val particleForces = new HashMap[Particle, ArrayBuffer[Vector2D]];
  val transformation = new Transformation;
  transformation += (moveParticle(fps, _:Particle));
  
  def addParticle(particle:Particle) = {
    particles.add(particle);
    particleForces.put(particle, new ArrayBuffer);
  }
  
  override def act = {
    val timestep = 1000 / fps;
    log.info("Nature has been started with timestep " + timestep);
    loop {
      val cycleStart = System.currentTimeMillis;
      applyParticleForces();
      val events = transform();
      handleEvents(events);
      dispatchInputs();
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = timestep - cycleMillis;
      log.debug("Cycle finished! Took %d/%d ms".format(cycleMillis, timestep));
      Thread.sleep(if (cooldown > 0) cooldown else 0);
    }
  }
  
  private def applyParticleForces() = {
    log.debug("Forces application phase starts...");
    particleForces.foreach { case(particle, forces) => 
      applyForces(particle, forces);
      forces.clear();
    };
    log.debug("Forces application phase ended.");
  }
  
  private def transform() = {
    log.debug("Transformation phase starts...");
    val events = particles.flatMap(transformation(_));
    log.debug("Transformation phase ended. %d events generated.".format(events.size));
    events;
  }
  
  private def handleEvents(events:LinkedHashSet[Event]) = {
    log.debug("Event handling phase starts...");
    events.foreach(_.triggerListeners);
    log.debug("Event handling phase ended.");
  }
  
  private def dispatchInputs() = {
    log.debug("Input dispatching phase starts...");
    var endOfMailbox = false;
    while(!endOfMailbox) {
      receiveWithin(0) {
        case ExertForce(p, f) => particleForces.get(p).get += f;
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
    log.debug("Input dispatching phase ended.");
  }
}