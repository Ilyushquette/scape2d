package scape.scape2d.engine.motion.collision.detection.linear

import scala.collection.Iterable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Sweepable
import scala.collection.mutable.HashSet
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.geom.partition.Node

class QuadTreeLinearMotionCollisionDetector[T <: Movable with Formed[_ <: Sweepable[_]]](
  val bounds:AxisAlignedRectangle,
  val detectionStrategy:LinearMotionCollisionDetectionStrategy[T]
) extends FiniteSpaceLinearMotionCollisionDetector[T] {
  type Tree = Node[LinearSweepFormingMovable[T]];
  private val treeCreationListeners = HashSet[Tree => Unit]();
  
  def detect(entities:Iterable[T], timestep:Double) = {
    val tree = new QuadTree[LinearSweepFormingMovable[T]](bounds, 4);
    entities.foreach(e => tree.insert(LinearSweepFormingMovable[T](e, timestep)));
    treeCreationListeners.foreach(_(tree));
    val collisions = detectNodeCollisions(tree, timestep);
    collisions.iterator;
  }
  
  private def detectNodeCollisions(tree:Tree, timestep:Double):Iterable[CollisionEvent[T]] = {
    if(tree.entities.isEmpty)
      tree.nodes.flatMap(detectNodeCollisions(_, timestep));
    else {
      val subEntityDetections = detectSubEntityCollisions(tree, timestep);
      val entityDetections = detectEntityCollisions(tree, timestep);
      val detections = subEntityDetections ++ entityDetections;
      val nodeCollisions = detections.collect {
        case (e1, e2, Some(t)) => CollisionEvent((e1, e2), t);
      }
      nodeCollisions ++ tree.nodes.flatMap(detectNodeCollisions(_, timestep));
    }
  }
  
  private def detectSubEntityCollisions(tree:Tree, timestep:Double) = {
    for (swept <- tree.entities; subSwept <- tree.nodes.flatMap(_.subEntities))
    yield (swept.entity, subSwept.entity, detectionStrategy.detect(swept, subSwept, timestep));
  }
  
  private def detectEntityCollisions(tree:Tree, timestep:Double) = {
   val combinations = tree.entities.toSeq.combinations(2);
   combinations.map(combination => {
     val swept1 = combination(0);
     val swept2 = combination(1);
     (swept1.entity, swept2.entity, detectionStrategy.detect(swept1, swept2, timestep));
   });
  }
  
  def addTreeCreationListener(listener:Tree => Unit) = treeCreationListeners += listener;
  
  def removeTreeCreationListener(listener:Tree => Unit) = treeCreationListeners -= listener;
}