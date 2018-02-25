package scape.scape2d.engine.motion.collision.detection.linear

import scala.collection.mutable.HashSet
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.geom.Formed

case class TreeLinearMotionCollisionDetector[T <: Movable with Formed[_ <: Sweepable[_]]](
  treeFactory:() => Node[LinearSweepFormingMovable[T]],
  detectionStrategy:LinearMotionCollisionDetectionStrategy[T]
) extends LinearMotionCollisionDetector[T] {
  private val treeCreationListeners = HashSet[Node[LinearSweepFormingMovable[T]] => Unit]();
  
  def detect(movables:Iterable[T], timestep:Double) = {
    val tree = treeFactory();
    movables.foreach(m => tree.insert(LinearSweepFormingMovable[T](m, timestep)));
    treeCreationListeners.foreach(_(tree));
    val collisions = detectNodeCollisions(tree, timestep);
    collisions.iterator;
  }
  
  private def detectNodeCollisions(tree:Node[LinearSweepFormingMovable[T]], timestep:Double):Iterable[CollisionEvent[T]] = {
    if(!tree.entities.isEmpty) {
      val entityDetections = detectEntityCollisions(tree, timestep);
      val subEntityDetections = detectSubEntityCollisions(tree, timestep);
      val detections = entityDetections ++ subEntityDetections;
      val nodeCollisions = detections.flatten;
      nodeCollisions ++ tree.nodes.flatMap(detectNodeCollisions(_, timestep));
    }else tree.nodes.flatMap(detectNodeCollisions(_, timestep));
  }
  
  private def detectEntityCollisions(tree:Node[LinearSweepFormingMovable[T]], timestep:Double) = {
    for {
      sweepFormingMovable1 <- tree.entities
      sweepFormingMovable2 <- tree.entities
      if(sweepFormingMovable1.entity != sweepFormingMovable2.entity)
    } yield detect(sweepFormingMovable1.entity, sweepFormingMovable2.entity, timestep);
  }
  
  private def detectSubEntityCollisions(tree:Node[LinearSweepFormingMovable[T]], timestep:Double) = {
    for(sweepFormingMovable <- tree.entities; subSweepFormingMovable <- tree.nodes.flatMap(_.subEntities))
    yield detect(sweepFormingMovable.entity, subSweepFormingMovable.entity, timestep);
  }
  
  private def detect(movable1:T, movable2:T, timestep:Double) = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(time => CollisionEvent((movable1, movable2), time));
  }
  
  def onTreeCreation(listener:Node[LinearSweepFormingMovable[T]] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[LinearSweepFormingMovable[T]] => Unit) = treeCreationListeners -= listener;
}