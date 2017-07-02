package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle

trait FiniteSpaceCollisionDetector[T <: Movable[T]] extends CollisionDetector[T] {
  def bounds:AxisAlignedRectangle;
}