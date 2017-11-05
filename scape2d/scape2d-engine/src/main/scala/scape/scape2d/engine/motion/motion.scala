package scape.scape2d.engine

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.Movable

package object motion {
  def getPositionAfter(movable:Movable, timestep:Double) = {
    if(timestep <= 0) throw new IllegalArgumentException("Time is irreversible. Timestep=" + timestep);
    if(movable.velocity.magnitude > 0) {
      val mpms = scaleVelocity(movable.velocity, timestep);
      movable.position.displace(mpms.components);
    }else movable.position;
  }
  
  def scaleVelocity(velocity:Vector2D, timestep:Double) = {
    val timescale = 1000 / timestep;
    new Vector2D(velocity.magnitude / timescale, velocity.angle);
  }
}