package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.motion.collision.RichCollisionEvent

case class BruteForcePosterioriRichCollisionDetector[S <: FiniteShape](
  detectionStrategy:DiscreteContactDetectionStrategy[S]
) extends PosterioriRichCollisionDetector[S] {
  def detect[T <: Movable[_ <: S]](movables:Set[T], timestep:Duration):Set[RichCollisionEvent[T]] = {
    val movableCombos = Combination2.selectFrom(movables);
    movableCombos.flatMap(detect(_, timestep));
  }
  
  def detect[T <: Movable[_ <: S]](movables:Combination2[T, T], timestep:Duration):Option[RichCollisionEvent[T]] = {
    val contactContainer = detectionStrategy.detect(movables._1, movables._2, timestep);
    contactContainer.map(RichCollisionEvent(movables, _));
  }
}