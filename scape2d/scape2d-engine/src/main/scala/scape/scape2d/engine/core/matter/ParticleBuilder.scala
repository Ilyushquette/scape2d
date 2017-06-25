package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D

case class ParticleBuilder(
  position:Point = Point.origin,
  radius:Double = 1,
  mass:Double = 1,
  velocity:Vector2D = new Vector2D(),
  forces:Array[Vector2D] = Array.empty
) {
  def at(p:Point) = copy(position = p);
  
  def withRadius(r:Double) = copy(radius = r);
  
  def withMass(m:Double) = copy(mass = m);
  
  def withVelocity(v:Vector2D) = copy(velocity = v);
  
  def withForces(fs:Array[Vector2D]) = copy(forces = fs);
  
  def build = new Particle(position, radius, mass, velocity, forces);
}