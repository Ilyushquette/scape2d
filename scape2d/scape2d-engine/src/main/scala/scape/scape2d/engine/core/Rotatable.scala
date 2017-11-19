package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point

trait Rotatable extends Volatile {
  /**
   * in meters
   */
  def center:Point;
  
  /**
   * pseudovector in radians per second
   */
  def angularVelocity:Double;
  
  private[core] def setAngularVelocity(newAngularVelocity:Double);
  
  def movables:Set[_ <: Movable];
}