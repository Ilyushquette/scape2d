package scape.scape2d.engine.motion.collision.detection

import scala.collection.immutable.Set
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.motion.collision.CollisionEvent

case class TreePosterioriCollisionDetector[T <: Movable with Formed[_ <: Shape]](
  treeFactory:() => Node[T]
) extends PosterioriCollisionDetector[T] {
  def detect(movables:Set[T]) = {
    val tree = treeFactory();
    movables.foreach(tree.insert);
    val combinations = Combination2.selectFrom(tree);
    val overlapping = combinations.filter(combination => combination._1.shape intersects combination._2.shape);
    overlapping.map(CollisionEvent(_, 0));
  }
}