package scape.scape2d.engine.core

import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D

trait Movable {
  private[core] val motionListeners = new LinkedHashSet[(Point2D, this.type) => Unit];
  
  def position:Point2D;
  
  def velocity:Vector2D;
  
  def addMotionListener(listener:(Point2D, this.type) => Unit) = motionListeners.add(listener);
  
  def removeMotionListener(listener:(Point2D, this.type) => Unit) = motionListeners.remove(listener);
}