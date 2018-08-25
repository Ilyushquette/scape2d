package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

trait CollisionDetectionStrategy[T <: Movable[_ <: Shape]]
extends LinearMotionCollisionDetectionStrategy[T] with RotationalCollisionDetectionStrategy[T] {
  def detect(movable1:T, movable2:T, timestep:Duration):Option[Duration];
}