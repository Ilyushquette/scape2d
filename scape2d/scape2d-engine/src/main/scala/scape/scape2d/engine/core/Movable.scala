package scape.scape2d.engine.core

import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D

trait Movable {  
  def position:Point2D;
  
  private[core] def setPosition(nextPosition:Point2D);
  
  def velocity:Vector2D;
  
  private[core] def setVelocity(newVelocity:Vector2D);
}