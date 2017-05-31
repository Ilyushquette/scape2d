package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.Point2D
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.LinkedHashSet

case class ParticleBuilder(
  position:Point2D = Point2D.origin,
  radius:Double = 1,
  mass:Double = 1,
  velocity:Vector2D = new Vector2D(),
  forces:ArrayBuffer[Vector2D] = new ArrayBuffer
) {
  def at(p:Point2D) = copy(position = p);
  
  def withRadius(r:Double) = copy(radius = r);
  
  def withMass(m:Double) = copy(mass = m);
  
  def withVelocity(v:Vector2D) = copy(velocity = v);
  
  def withForces(fs:ArrayBuffer[Vector2D]) = copy(forces = fs);
  
  def build = new Particle(position, radius, mass, velocity, forces);
}