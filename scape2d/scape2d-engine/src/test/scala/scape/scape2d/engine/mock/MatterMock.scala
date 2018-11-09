package scape.scape2d.engine.mock

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.angular.MomentOfInertia
import scape.scape2d.engine.motion.linear.Velocity

class MatterMock[T <: FiniteShape](
  var shape:T,
  var velocity:Velocity = Velocity.zero,
  var mass:Mass = Mass.zero,
  var rotatable:Option[Rotatable] = None
) extends Matter[T] {
  def position = shape.center;
  
  def setShape(shape:T) = this.shape = shape;
  
  def setVelocity(velocity:Velocity) = this.velocity = velocity;
  
  def momentOfInertia = MomentOfInertia(mass, shape);
  
  def snapshot = new MatterMock(shape, velocity, mass);
}