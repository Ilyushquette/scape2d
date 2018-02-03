package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle

trait FiniteSpaceLinearMotionCollisionDetector[T <: Movable] extends LinearMotionCollisionDetector[T] {
  def bounds:AxisAlignedRectangle;
}