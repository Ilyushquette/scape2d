package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration

trait CollisionDetectionStrategy[T <: Movable]
extends LinearMotionCollisionDetectionStrategy[T] with RotationalCollisionDetectionStrategy[T] {
  def detect(movable1:T, movable2:T, timestep:Duration):Option[Duration];
}