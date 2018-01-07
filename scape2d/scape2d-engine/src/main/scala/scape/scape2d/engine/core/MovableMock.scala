package scape.scape2d.engine.core

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

private[engine] class MovableMock(
  var shape:Circle,
  var velocity:Vector,
  private var _rotatable:Option[Rotatable])
extends Movable with Formed[Circle] {
  // this default constructor is only exists for cglib proxy support
  def this() = this(Circle(Point.origin, 0), Vector(), None);
  
  def this(position:Point, velocity:Vector, rotatable:Option[Rotatable]) = {
    this(Circle(position, 0), velocity, rotatable);
  }
  
  def position = shape.center;
  
  def setPosition(nextPosition:Point) = shape = shape.copy(nextPosition, shape.radius);
  
  def setVelocity(newVelocity:Vector) = velocity = newVelocity;
  
  def rotatable = _rotatable;
  
  def setRotatable(newRotatable:Rotatable) = _rotatable = Some(newRotatable);
  
  def snapshot = new MovableMock(shape, velocity, rotatable);
}