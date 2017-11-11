package scape.scape2d.engine.motion

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.Movable

package object linear {
  def getPositionAfter(movable:Movable, timestep:Double) = {
    if(timestep <= 0) throw new IllegalArgumentException("Time is irreversible. Timestep=" + timestep);
    if(movable.velocity.magnitude > 0) {
      val metersPerTimestep = asMetersPerTimestep(movable.velocity, timestep);
      movable.position.displace(metersPerTimestep.components);
    }else movable.position;
  }
  
  def asMetersPerTimestep(velocity:Vector2D, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    new Vector2D(velocity.magnitude / stepsPerSecond, velocity.angle);
  }
  
  def asMetersPerSecond(metersPerTimestep:Vector2D, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    new Vector2D(metersPerTimestep.magnitude * stepsPerSecond, metersPerTimestep.angle);
  }
}