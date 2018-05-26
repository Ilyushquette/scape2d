package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.motion.linear.Velocity

case class ParticleBuilder(
  shape:Circle = Circle(Point.origin, 1),
  mass:Double = 1,
  velocity:Velocity = Velocity.zero,
  force:Vector = Vector.zero
) {
  /**
   * position in meters, radius in meters
   */
  def as(s:Circle) = copy(shape = s);
  
 /**
  * in kilograms
  */
  def withMass(m:Double) = copy(mass = m);
  
  def withVelocity(v:Velocity) = copy(velocity = v);
  
  def withForce(f:Vector) = copy(force = f);
  
  def build = new Particle(Particle.nextId, shape, mass, velocity, force);
}