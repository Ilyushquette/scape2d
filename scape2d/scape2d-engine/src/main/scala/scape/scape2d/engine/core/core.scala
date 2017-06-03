package scape.scape2d.engine

import org.apache.log4j.Logger
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.getPositionAfter
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector2D

package object core {
  private val log = Logger.getLogger(getClass);
  
  private[core] def integrateMotion(movable:Movable, timestep:Double) = {
    log.debug("Movable at %s with velocity %s".format(movable.position, movable.velocity));
    val nextPosition = getPositionAfter(movable, timestep);
    if(movable.position != nextPosition) {
      movable.setPosition(nextPosition);
      log.debug("Moved to " + nextPosition);
    }
  }
  
  /**
   * Since each force in the particle is a representation of impulse J = N x timestep,
   * acceleration in meters per second per timestep too.
   * In the other words: all forces collected since last time integration
   * Final velocity of the particle in meters per second.
   */
  private[core] def integrateAcceleration(particle:Particle) = {
    val forces = particle.forces;
    if(!forces.isEmpty) {
      val netforce = forces.reduce(_ + _);
      val acceleration = new Vector2D(netforce.magnitude / particle.mass, netforce.angle);
      log.debug("Netforce %s sum of %s caused acceleration %s".format(netforce, forces, acceleration));
      particle.velocity = particle.velocity + acceleration;
      forces.clear();
    }
  }
}