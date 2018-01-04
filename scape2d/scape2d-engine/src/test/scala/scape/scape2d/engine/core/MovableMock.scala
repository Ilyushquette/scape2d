package scape.scape2d.engine.core

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point

class MovableMock(
  var position:Point,
  var velocity:Vector,
  private var _rotatable:Option[Rotatable])
extends Movable {
  // this default constructor is only exists for cglib proxy support
  def this() = this(Point.origin, Vector(), None);
  
  def setPosition(nextPosition:Point) = position = nextPosition;
  
  def setVelocity(newVelocity:Vector) = velocity = newVelocity;
  
  def rotatable = _rotatable;
  
  def setRotatable(newRotatable:Rotatable) = _rotatable = Some(newRotatable);
  
  def snapshot = new MovableMock(position, velocity, rotatable);
}