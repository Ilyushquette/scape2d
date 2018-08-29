package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity

/**
 * NOTE: Type parameter T which has upper bound of FiniteShape
 * ALSO has lower bound of Null to allow package private default constructor to initialize
 * rigid body with null shape in order to support cglib proxying
 */
class RigidBody[T >: Null <: FiniteShape] private[matter](
  var _shape:T,
  val mass:Mass,
  var _velocity:Velocity,
  var _angularVelocity:AngularVelocity
) extends Movable[T] with Rotatable {
  // this package private default constructor exists only for cglib proxy support
  private[matter] def this() = this(null, Kilogram, Velocity.zero, AngularVelocity.zero);
  
  def position = _shape.center;
  
  def center = position;
  
  def shape = _shape;
  
  private[core] def setShape(newShape:T) = _shape = newShape;
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Velocity) = _velocity = newVelocity;
  
  def rotatable = Some(this);
  
  def angularVelocity = _angularVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:AngularVelocity) = _angularVelocity = newAngularVelocity;
  
  def movables = Set(this);
  
  def snapshot = snapshot();
  
  def snapshot(shape:T = _shape,
               mass:Mass = this.mass,
               velocity:Velocity = _velocity,
               angularVelocity:AngularVelocity = _angularVelocity) = {
    new RigidBody(shape, mass, velocity, angularVelocity);
  }
}