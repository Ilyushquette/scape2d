package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.partition.Node
import scape.scape2d.engine.motion.MotionBounds
import scape.scape2d.engine.motion.collision.CollisionEvent
import scala.collection.mutable.HashSet
import scape.scape2d.engine.core.Identifiable

case class TreeCollisionDetector[T <: Movable with Formed[_ <: Shape] with Identifiable](
  treeFactory:() => Node[MotionBounds[T]], 
  detectionStrategy:CollisionDetectionStrategy[T]
) extends CollisionDetector[T] {
  private val treeCreationListeners = HashSet[Node[MotionBounds[T]] => Unit]();
  
  def detect(movables:Set[T], timestep:Double) = {
    val tree = treeFactory();
    movables.foreach(m => tree.insert(MotionBounds(m, timestep)));
    treeCreationListeners.foreach(_(tree));
    detectNodeCollisions(tree, timestep);
  }
  
  private def detectNodeCollisions(tree:Node[MotionBounds[T]], timestep:Double):List[CollisionEvent[T]] = {
    if(!tree.entities.isEmpty) {
      val entityDetections = detectEntityCollisions(tree, timestep);
      val subEntityDetections = detectSubEntityCollisions(tree, timestep);
      val detections = entityDetections ++ subEntityDetections;
      val nodeCollisions = detections.flatten;
      nodeCollisions ++ tree.nodes.flatMap(detectNodeCollisions(_, timestep));
    }else tree.nodes.flatMap(detectNodeCollisions(_, timestep));
  }
  
  private def detectEntityCollisions(tree:Node[MotionBounds[T]], timestep:Double) = {
    for {
      motionBounds1 <- tree.entities
      motionBounds2 <- tree.entities
      if(motionBounds1.movable != motionBounds2.movable)
    } yield detect(motionBounds1.movable, motionBounds2.movable, timestep);
  }
  
  private def detectSubEntityCollisions(tree:Node[MotionBounds[T]], timestep:Double) = {
    for(motionBounds <- tree.entities; subMotionBounds <- tree.nodes.flatMap(_.subEntities))
    yield detect(motionBounds.movable, subMotionBounds.movable, timestep);
  }
  
  private def detect(movable1:T, movable2:T, timestep:Double) = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(time => CollisionEvent((movable1, movable2), time));
  }
  
  def onTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners += listener;
  
  def offTreeCreation(listener:Node[MotionBounds[T]] => Unit) = treeCreationListeners -= listener;
}