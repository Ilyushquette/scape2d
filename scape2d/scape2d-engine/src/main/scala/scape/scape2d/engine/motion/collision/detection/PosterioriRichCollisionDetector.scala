package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.time.Duration

trait PosterioriRichCollisionDetector[S <: FiniteShape] {
  def detect[T <: Movable[_ <: S]](movables:Set[T], timestep:Duration):Set[RichCollisionEvent[T]];
}