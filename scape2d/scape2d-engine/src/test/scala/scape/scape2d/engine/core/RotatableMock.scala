package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point

class RotatableMock(
  val center:Point,
  private var _angularVelocity:Double,
  val movables:Set[MovableMock])
extends Rotatable {
  movables.foreach(_.setRotatable(this));
  
  def angularVelocity = _angularVelocity;
  
  def setAngularVelocity(newAngularVelocity:Double) = _angularVelocity = newAngularVelocity;
  
  def snapshot = new RotatableMock(center, _angularVelocity, movables);
}