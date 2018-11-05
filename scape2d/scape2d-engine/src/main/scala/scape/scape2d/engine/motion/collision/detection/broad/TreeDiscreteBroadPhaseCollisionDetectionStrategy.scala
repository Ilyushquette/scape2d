package scape.scape2d.engine.motion.collision.detection.broad

import scala.collection.mutable.HashSet

import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.core.Movable

class TreeDiscreteBroadPhaseCollisionDetectionStrategy[T <: Movable[_ <: FiniteShape] with Identifiable](
  treeFactory:() => Node[T]
) extends DiscreteBroadPhaseCollisionDetectionStrategy[T] {
  private val treeCreationListeners = HashSet[Node[T] => Unit]();
  
  def prune(movables:Set[T]) = {
    val tree = treeFactory();
    movables.foreach(tree.insert);
    treeCreationListeners.foreach(_(tree));
    Combination2.selectFrom(tree);
  }
  
  def onTreeCreation(listener:Node[T] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[T] => Unit) = treeCreationListeners -= listener;
  
  def offTreeCreation() = treeCreationListeners.clear();
}