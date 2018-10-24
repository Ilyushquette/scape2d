package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed

trait Movable[T <: Shape] extends Locable with Formed[T] with Volatile {
  private[core] def setShape(nextShape:T);
  
  def velocity:Velocity;
  
  private[core] def setVelocity(newVelocity:Velocity);
  
  def isMovingLinearly = velocity.vector.magnitude > 0;
  
  def rotatable:Option[Rotatable];
  
  def isRotating = rotatable.map(_.angularVelocity != AngularVelocity.zero).getOrElse(false);
  
  def isStationary = !isMovingLinearly && !isRotating;
  
  def snapshot:Movable[T];
}