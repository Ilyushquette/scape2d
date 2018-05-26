package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.Velocity

trait Movable extends Volatile {
  /**
   * in meters
   */
  def position:Point;
  
  private[core] def setPosition(nextPosition:Point);
  
  def velocity:Velocity;
  
  private[core] def setVelocity(newVelocity:Velocity);
  
  def isMovingLinearly = velocity.vector.magnitude > 0;
  
  def rotatable:Option[Rotatable];
  
  def isRotating = rotatable.map(_.angularVelocity != 0).getOrElse(false);
  
  def isStationary = !isMovingLinearly && !isRotating;
}