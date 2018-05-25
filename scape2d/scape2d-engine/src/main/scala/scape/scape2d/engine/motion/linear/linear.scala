package scape.scape2d.engine.motion

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second

package object linear {
  def positionForTimeOf(movable:Movable):(Duration => Point) = {
    val position = movable.position;
    val velocity = movable.velocity;
    if(velocity.magnitude > 0) {
      timestep => position + asMetersPerTimestep(velocity, timestep);
    }else _ => position;
  }
  
  def asMetersPerTimestep(velocity:Vector, timestep:Duration) = {
    val stepsPerSecond = Second / timestep;
    Vector(velocity.magnitude / stepsPerSecond, velocity.angle);
  }
  
  def asMetersPerSecond(metersPerTimestep:Vector, timestep:Duration) = {
    val stepsPerSecond = Second / timestep;
    Vector(metersPerTimestep.magnitude * stepsPerSecond, metersPerTimestep.angle);
  }
}