package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity

/**
 * NOTE: See {@link RigidBody} javadoc as to why type parameter T lower bound to Null
 */
case class RigidBodyBuilder[T >: Null <: FiniteShape](
  shape:T,
  mass:Mass = Kilogram,
  velocity:Velocity = Velocity.zero,
  angularVelocity:AngularVelocity = AngularVelocity.zero,
  restitutionCoefficient:Double = 0.5
) {
  def as(s:T) = copy(shape = s);
  
  def withMass(m:Mass) = copy(mass = m);
  
  def withVelocity(v:Velocity) = copy(velocity = v);
  
  def withAngularVelocity(av:AngularVelocity) = copy(angularVelocity = av);
  
  def withRestitutionCoefficient(rc:Double) = copy(restitutionCoefficient = rc);
  
  def build() = new RigidBody(RigidBody.nextId, shape, mass, velocity, angularVelocity, restitutionCoefficient);
}