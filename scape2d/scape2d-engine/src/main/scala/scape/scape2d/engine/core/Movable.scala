package scape.scape2d.engine.core

import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector

trait Movable extends Volatile {
  /**
   * in meters
   */
  def position:Point;
  
  private[core] def setPosition(nextPosition:Point);
  
  /**
   * magnitude in meters, angle in degrees (meters per second at the direction)
   */
  def velocity:Vector;
  
  private[core] def setVelocity(newVelocity:Vector);
  
  def isMovingLinearly = velocity.magnitude > 0;
  
  def rotatable:Option[Rotatable];
  
  def isRotating = rotatable.map(_.angularVelocity != 0).getOrElse(false);
  
  def isStationary = !isMovingLinearly && !isRotating;
}