package scape.scape2d.engine.motion

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

package object linear {
  def displacementForTimeOf(movable:Movable[_ <: Shape]):(Duration => Vector) = {
    val v = movable.velocity;
    if(movable.isMovingLinearly) v forTime _;
    else _ => Vector.zero;
  }
  
  def positionForTimeOf(movable:Movable[_ <: Shape]):(Duration => Point) = {
    val p = movable.position;
    val vt = displacementForTimeOf(movable);
    t => p displacedBy vt(t);
  }
  
  def displacedShapeForTimeOf[T <: Shape](movable:Movable[T]):(Duration => T) = {
    val shape = movable.shape;
    val vt = displacementForTimeOf(movable);
    t => shape.displacedBy(vt(t)).asInstanceOf[T];
  }
}