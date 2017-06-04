package scape.scape2d.engine.core

import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D

trait Movable {  
  /**
   * in meters
   */
  def position:Point2D;
  
  private[core] def setPosition(nextPosition:Point2D);
  
  /**
   * magnitude in meters, angle in degrees (meters per second at the direction)
   */
  def velocity:Vector2D;
  
  private[core] def setVelocity(newVelocity:Vector2D);
}