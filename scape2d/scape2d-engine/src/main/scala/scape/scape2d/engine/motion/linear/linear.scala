package scape.scape2d.engine.motion

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.Movable

package object linear {
  def getPostLinearMotionPosition(movable:Movable, timestep:Double) = {
    if(movable.velocity.magnitude > 0) {
      val metersPerTimestep = asMetersPerTimestep(movable.velocity, timestep);
      movable.position + metersPerTimestep;
    }else movable.position;
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