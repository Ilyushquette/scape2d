package scape.scape2d.engine.motion

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point

package object linear {
  def positionForTimeOf(movable:Movable):(Double => Point) = {
    val position = movable.position;
    val velocity = movable.velocity;
    if(velocity.magnitude > 0) {
      timestep => position + asMetersPerTimestep(velocity, timestep);
    }else _ => position;
  }
  
  def asMetersPerTimestep(velocity:Vector, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    Vector(velocity.magnitude / stepsPerSecond, velocity.angle);
  }
  
  def asMetersPerSecond(metersPerTimestep:Vector, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    Vector(metersPerTimestep.magnitude * stepsPerSecond, metersPerTimestep.angle);
  }
}