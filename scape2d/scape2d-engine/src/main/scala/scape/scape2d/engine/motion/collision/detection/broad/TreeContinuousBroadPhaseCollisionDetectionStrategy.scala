package scape.scape2d.engine.motion.collision.detection.broad

import scala.collection.mutable.HashSet

import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.motion.MotionBounds
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.core.Movable

class TreeContinuousBroadPhaseCollisionDetectionStrategy[T <: Movable[_ <: FiniteShape] with Identifiable](
  treeFactory:() => Node[MotionBounds[T]]
) extends ContinuousBroadPhaseCollisionDetectionStrategy[T] {
  private val treeCreationListeners = HashSet[Node[MotionBounds[T]] => Unit]();
  
  def prune(movables:Set[T], timestep:Duration) = {
    val tree = treeFactory();
    movables.foreach(m => tree.insert(MotionBounds(m, timestep)));
    treeCreationListeners.foreach(_(tree));
    val treeCombinations = Combination2.selectFrom(tree);
    treeCombinations.map(combo => Combination2(combo._1.movable, combo._2.movable));
  }
  
  def onTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners -= listener;
  
  def offTreeCreation() = treeCreationListeners.clear();
}