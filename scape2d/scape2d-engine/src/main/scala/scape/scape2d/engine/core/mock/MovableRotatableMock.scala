package scape.scape2d.engine.core.mock

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity

class MovableRotatableMock[T <: FiniteShape](
  var shape:T,
  var velocity:Velocity,
  var angularVelocity:AngularVelocity
) extends Movable[T] with Rotatable {
  def position = shape.center;
  
  def center = position;
  
  private[core] def setShape(newShape:T) = shape = newShape;
  
  private[core] def setVelocity(newVelocity:Velocity) = velocity = newVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:AngularVelocity) = angularVelocity = newAngularVelocity;
  
  val rotatable = Some(this);
  
  val movables = Set(this);
  
  def snapshot = new MovableRotatableMock(shape, velocity, angularVelocity);
}