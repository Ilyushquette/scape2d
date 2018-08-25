package scape.scape2d.engine.motion

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

package object linear {
  def positionForTimeOf(movable:Movable[_ <: Shape]):(Duration => Point) = {
    val position = movable.position;
    val velocity = movable.velocity;
    if(movable.isMovingLinearly) {
      timestep => {
        val displacementVector = velocity.forTime(timestep);
        position + displacementVector;
      }
    }else _ => position;
  }
}