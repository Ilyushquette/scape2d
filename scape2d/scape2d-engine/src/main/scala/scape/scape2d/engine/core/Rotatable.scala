package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.rotational.AngularVelocity
import scape.scape2d.engine.geom.shape.Shape

trait Rotatable extends Volatile {
  /**
   * in meters
   */
  def center:Point;

  def angularVelocity:AngularVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:AngularVelocity);
  
  def movables:Set[_ <: Movable[_ <: Shape]];
}