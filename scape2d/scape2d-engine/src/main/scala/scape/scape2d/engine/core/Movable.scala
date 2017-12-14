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
  
  def rotatable:Option[Rotatable];
}