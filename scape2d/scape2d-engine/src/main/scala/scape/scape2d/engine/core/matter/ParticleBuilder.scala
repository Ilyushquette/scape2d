package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Circle

case class ParticleBuilder(
  shape:Circle = Circle(Point.origin, 1),
  mass:Double = 1,
  velocity:Vector2D = new Vector2D(),
  forces:Array[Vector2D] = Array.empty
) {
  /**
   * position in meters, radius in centimeters
   */
  def as(s:Circle) = copy(shape = s);
  
 /**
  * in kilograms
  */
  def withMass(m:Double) = copy(mass = m);
  
  def withVelocity(v:Vector2D) = copy(velocity = v);
  
  def withForces(fs:Array[Vector2D]) = copy(forces = fs);
  
  def build = new Particle(shape, mass, velocity, forces);
}