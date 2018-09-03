package scape.scape2d.engine.motion.collision.detection

import scala.collection.immutable.Set
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.motion.collision.CollisionEvent
import scala.collection.mutable.HashSet
import scape.scape2d.engine.time.Duration

case class TreePosterioriCollisionDetector[T <: Movable[_ <: Shape]](
  treeFactory:() => Node[T]
) extends PosterioriCollisionDetector[T] {
  private val treeCreationListeners = HashSet[Node[T] => Unit]();
  
  def detect(movables:Set[T]) = {
    val tree = treeFactory();
    movables.foreach(tree.insert);
    treeCreationListeners.foreach(_(tree));
    val combinations = Combination2.selectFrom(tree);
    val overlapping = combinations.filter(combination => combination._1.shape intersects combination._2.shape);
    overlapping.map(CollisionEvent(_, Duration.zero));
  }
  
  def onTreeCreation(listener:Node[T] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[T] => Unit) = treeCreationListeners -= listener;
  
  def offTreeCreation() = treeCreationListeners.clear();
}