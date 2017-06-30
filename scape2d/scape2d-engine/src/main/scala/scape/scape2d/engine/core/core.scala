package scape.scape2d.engine

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.getPositionAfter
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector2D

package object core {
  private[core] def integrateMotion(movable:Movable[_], timestep:Double) = {
    val nextPosition = getPositionAfter(movable, timestep);
    if(movable.position != nextPosition) movable.setPosition(nextPosition);
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
      particle.setVelocity(particle.velocity + acceleration);
      particle.setForces(Array.empty);
    }
  }
}