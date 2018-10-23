package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.motion.collision.ContactContainer
import scape.scape2d.engine.time.Duration

trait DiscreteContactDetectionStrategy[S <: FiniteShape] {
  def detect(movable1:Movable[_ <: S], movable2:Movable[_ <: S], timestep:Duration):Option[ContactContainer];
}