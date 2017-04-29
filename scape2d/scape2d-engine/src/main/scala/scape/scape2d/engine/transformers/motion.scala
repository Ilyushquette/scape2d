package scape.scape2d.engine

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom._
import org.apache.log4j.Logger
import scape.scape2d.engine.matter.MotionEvent

package object motion {
  private val log = Logger.getLogger(getClass);
  
  private[engine] def moveParticle(fps:Int, particle:Particle) = {
    log.debug("Particle at %s with velocity %s".format(particle.position, particle.velocity));
    if(particle.velocity.magnitude > 0) {
      val oldPosition = particle.position.clone;
      val scaleMpsToMpms = scaleMagnitudePerSecond(fps, _:Vector2D);
      val move = scaleMpsToMpms andThen calculateComponents andThen particle.position.displace _;
      move(particle.velocity);
      log.debug("Moved particle to %s. Prepared update pack".format(particle.position));
      List(new MotionEvent(oldPosition, particle));
    }else Nil;
  }
  
  /**
   * Acceleration in meters per second per interval_between_instants.
   * Final velocity of the particle in meters per second.
   */
  private[engine] def applyForces(particle:Particle, forces:Iterable[Vector2D]) = {
    val netforce = sum(forces);
    val acceleration = new Vector2D(netforce.magnitude / particle.mass, netforce.angle);
    log.debug("Netforce %s sum of %s caused acceleration %s".format(netforce, forces, acceleration));
    particle.velocity = sum(List(particle.velocity, acceleration));
  }
  
  private def scaleMagnitudePerSecond(fps:Int, vector:Vector2D) = {
    new Vector2D(vector.magnitude / fps, vector.angle);
  }
}