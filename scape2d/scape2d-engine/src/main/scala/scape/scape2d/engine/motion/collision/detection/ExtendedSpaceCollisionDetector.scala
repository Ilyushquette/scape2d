package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.motion.collision.Collision
import scala.collection.mutable.HashSet

class ExtendedSpaceCollisionDetector[T <: Movable[T] with Formed[_ <: Sweepable[_]]] (
  val coreDetector:FiniteSpaceCollisionDetector[T],
  val regionalDetectorFactory:AxisAlignedRectangle => CollisionDetector[T],
  val edgeCaseDetect:(T, T, Double) => Option[Double],
  val extension:Int)
extends FiniteSpaceCollisionDetector[T] {
  type Swept = SweepFormingMovable[T];
  
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
    val sweptEntities = entities.map(SweepFormingMovable(_, timestep));
    val outsideOfCore = sweptEntities.filterNot(coreDetector.bounds.contains(_));
    if(outsideOfCore.isEmpty) coreCollisions;
    else coreCollisions ++ detectOuterCollisions(outsideOfCore, sweptEntities, timestep);
  }
  
  private def detectOuterCollisions(outsideOfCore:Iterable[Swept], all:Iterable[Swept], timestep:Double) = {
    val buckets = regions.map(region => new Bucket(region, regionalDetectorFactory(region)));
    val notInBuckets = outsideOfCore.filterNot(swept => buckets.exists(_.insert(swept)));
    val onTheEdge = notInBuckets.filter(bounds.contains(_));
    val edgeDetections = for (swept <- onTheEdge; swept2 <- all)
                         yield (swept.entity, swept2.entity, edgeCaseDetect(swept, swept2, timestep));
    val edgeCollisions = edgeDetections.collect {
      case (e1, e2, Some(t)) => Collision((e1, e2), t);
    }
    val bucketCollisions = buckets.flatMap(_.detectCollisions(timestep));
    edgeCollisions ++ bucketCollisions;
  }
  
  private[ExtendedSpaceCollisionDetector] class Bucket(
    val bounds:AxisAlignedRectangle,
    val collisionDetector:CollisionDetector[T]) {
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