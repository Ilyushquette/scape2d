package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.motion.MotionBounds
import scape.scape2d.engine.motion.collision.CollisionEvent
import scala.collection.mutable.HashSet
import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.time.Duration

case class TreeCollisionDetector[T <: Movable with Formed[_ <: Shape] with Identifiable](
  treeFactory:() => Node[MotionBounds[T]], 
  detectionStrategy:CollisionDetectionStrategy[T]
) extends CollisionDetector[T] {
  private val treeCreationListeners = HashSet[Node[MotionBounds[T]] => Unit]();
  
  def detect(movables:Set[T], timestep:Duration):Set[CollisionEvent[T]] = {
    val tree = treeFactory();
    movables.foreach(m => tree.insert(MotionBounds(m, timestep)));
    treeCreationListeners.foreach(_(tree));
    val treeCombinations = Combination2.selectFrom(tree);
    treeCombinations.flatMap(c => detect(c._1.movable, c._2.movable, timestep));
  }
  
  def detect(checkables:Set[T], others:Set[T], timestep:Duration):Set[CollisionEvent[T]] = {
    val tree = treeFactory();
    val checkableMotionBounds = checkables.map(MotionBounds(_, timestep));
    checkableMotionBounds.foreach(tree.insert);
    others.foreach(m => tree.insert(MotionBounds(m, timestep)));
    treeCreationListeners.foreach(_(tree));
    checkableMotionBounds.flatMap(detectBranchCollisions(_, tree, timestep));
  }
  
  private def detectBranchCollisions(entity:MotionBounds[T], tree:Node[MotionBounds[T]], timestep:Duration) = {
    val nodeWithEntity = tree.findTreeNode(entity).get;
    val otherBranchEntities = nodeWithEntity.superEntities ++
                              (nodeWithEntity.entities - entity) ++
                              nodeWithEntity.subEntities;
    otherBranchEntities.flatMap(otherEntity => detect(entity.movable, otherEntity.movable, timestep));
  }
  
  private def detect(movable1:T, movable2:T, timestep:Duration):Option[CollisionEvent[T]] = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(time => CollisionEvent(movable1, movable2, time));
  }
  
  def onTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners -= listener;
}