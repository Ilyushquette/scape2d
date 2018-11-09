package scape.scape2d.engine.core.mock

import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.rotational.AngularVelocity

private[engine] class RotatableMock(
  val center:Point,
  private var _angularVelocity:AngularVelocity,
  val movables:Set[MovableMock])
extends Rotatable {
  movables.foreach(_.setRotatable(this));
  
  def angularVelocity = _angularVelocity;
  
  def setAngularVelocity(newAngularVelocity:AngularVelocity) = _angularVelocity = newAngularVelocity;
  
  def snapshot = new RotatableMock(center, _angularVelocity, movables);
}