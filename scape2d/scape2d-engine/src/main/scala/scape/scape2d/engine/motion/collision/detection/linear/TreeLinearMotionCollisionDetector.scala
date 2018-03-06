package scape.scape2d.engine.motion.collision.detection.linear

import scala.collection.mutable.HashSet
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.motion.linear.LinearSweepFormingMovable
import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.util.Combination2

case class TreeLinearMotionCollisionDetector[T <: Movable with Formed[_ <: Sweepable[_]] with Identifiable](
  treeFactory:() => Node[LinearSweepFormingMovable[T]],
  detectionStrategy:LinearMotionCollisionDetectionStrategy[T]
) extends LinearMotionCollisionDetector[T] {
  private val treeCreationListeners = HashSet[Node[LinearSweepFormingMovable[T]] => Unit]();
  
  def detect(movables:Iterable[T], timestep:Double) = {
    val tree = treeFactory();
    movables.foreach(m => tree.insert(LinearSweepFormingMovable[T](m, timestep)));
    treeCreationListeners.foreach(_(tree));
    val treeCombinations = Combination2.selectFrom(tree);
    val treeCollisions = treeCombinations.flatMap(c => detect(c._1.entity, c._2.entity, timestep));
    treeCollisions.iterator;
  }
  
  private def detect(movable1:T, movable2:T, timestep:Double) = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(time => CollisionEvent((movable1, movable2), time));
  }
  
  def onTreeCreation(listener:Node[LinearSweepFormingMovable[T]] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[LinearSweepFormingMovable[T]] => Unit) = treeCreationListeners -= listener;
}