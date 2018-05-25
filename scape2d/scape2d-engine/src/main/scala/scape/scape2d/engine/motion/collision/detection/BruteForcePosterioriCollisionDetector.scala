package scape.scape2d.engine.motion.collision.detection

import scala.collection.immutable.Set
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.time.Duration

case class BruteForcePosterioriCollisionDetector[T <: Movable with Formed[_ <: Shape]]()
extends PosterioriCollisionDetector[T] {
  def detect(movables:Set[T]) = {
    val combinations = Combination2.selectFrom(movables);
    val overlapping = combinations.filter(combination => combination._1.shape intersects combination._2.shape);
    overlapping.map(CollisionEvent(_, Duration.zero));
  }
}