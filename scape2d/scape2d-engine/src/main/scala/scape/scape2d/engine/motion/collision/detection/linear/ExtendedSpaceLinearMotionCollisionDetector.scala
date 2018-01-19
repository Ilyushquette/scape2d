package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scala.collection.mutable.HashSet

class ExtendedSpaceLinearMotionCollisionDetector[T <: Movable with Formed[_ <: Sweepable[_]]] (
  val coreDetector:FiniteSpaceLinearMotionCollisionDetector[T],
  val regionalDetectorFactory:AxisAlignedRectangle => LinearMotionCollisionDetector[T],
  val edgeCaseDetectionStrategy:LinearMotionCollisionDetectionStrategy[T],
  val extension:Int)
extends FiniteSpaceLinearMotionCollisionDetector[T] {
  type Swept = LinearSweepFormingMovable[T];
  
  val topLeft = AxisAlignedRectangle(Point(coreDetector.bounds.topLeft.x - extension, coreDetector.bounds.topLeft.y), extension, extension);
  val topCenter = AxisAlignedRectangle(coreDetector.bounds.topLeft, coreDetector.bounds.width, extension);
  val topRight = AxisAlignedRectangle(coreDetector.bounds.topRight, extension, extension);
  val centerLeft = AxisAlignedRectangle(Point(topLeft.topLeft.x, coreDetector.bounds.bottomLeft.y), extension, coreDetector.bounds.height);
  val centerRight = AxisAlignedRectangle(coreDetector.bounds.bottomRight, extension, coreDetector.bounds.height);
  val bottomLeft = AxisAlignedRectangle(Point(topLeft.topLeft.x, coreDetector.bounds.bottomLeft.y - extension), extension, extension);
  val bottomCenter = AxisAlignedRectangle(bottomLeft.bottomRight, coreDetector.bounds.width, extension);
  val bottomRight = AxisAlignedRectangle(bottomCenter.bottomRight, extension, extension);
  
  val regions = Set(topLeft, topCenter, topRight, centerLeft, centerRight, bottomLeft, bottomCenter, bottomRight);
  val bounds = AxisAlignedRectangle(bottomLeft.bottomLeft, extension * 2 + coreDetector.bounds.width, extension * 2 + coreDetector.bounds.height);
  
  def detect(entities:Iterable[T], timestep:Double) = {
    val coreCollisions = coreDetector.detect(entities, timestep);
    val sweptEntities = entities.map(LinearSweepFormingMovable(_, timestep));
    val outsideOfCore = sweptEntities.filterNot(coreDetector.bounds.contains(_));
    if(outsideOfCore.isEmpty) coreCollisions;
    else coreCollisions ++ detectOuterCollisions(outsideOfCore, sweptEntities, timestep);
  }
  
  private def detectOuterCollisions(outsideOfCore:Iterable[Swept], all:Iterable[Swept], timestep:Double) = {
    val buckets = regions.map(region => new Bucket(region, regionalDetectorFactory(region)));
    val notInBuckets = outsideOfCore.filterNot(swept => buckets.exists(_.insert(swept)));
    val onTheEdge = notInBuckets.filter(bounds.contains(_));
    val edgeDetections = for (swept <- onTheEdge; swept2 <- all)
                         yield (swept.entity, swept2.entity, edgeCaseDetectionStrategy.detect(swept, swept2, timestep));
    val edgeCollisions = edgeDetections.collect {
      case (e1, e2, Some(t)) => CollisionEvent((e1, e2), t);
    }
    val bucketCollisions = buckets.flatMap(_.detectCollisions(timestep));
    edgeCollisions ++ bucketCollisions;
  }
  
  private[ExtendedSpaceLinearMotionCollisionDetector] class Bucket(
    val bounds:AxisAlignedRectangle,
    val collisionDetector:LinearMotionCollisionDetector[T]) {
    val sweptEntities:HashSet[Swept] = HashSet.empty;
    
    def insert(sweptEntity:Swept) = {
      bounds.contains(sweptEntity.shape) && (sweptEntities add sweptEntity);
    }
    
    def detectCollisions(timestep:Double) = {
      val entities = sweptEntities.map(_.entity);
      collisionDetector.detect(entities, timestep);
    }
  }
}