package scape.scape2d.engine.motion.collision.detection.linear

import com.google.common.math.DoubleMath
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.motion.collision.detection._

object LinearMotionCollisionDetectionStrategyValidator {
  def check(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    checkTrajectoriesOverlayFrontalCollision(detectionStrategy);
    checkTrajectoriesOverlayFrontalNoCollision(detectionStrategy);
    checkTrajectoriesOverlayUnidirectionalNoCollision(detectionStrategy);
    checkTrajectoriesOverlayUnidirectionalCollision(detectionStrategy);
    checkTrajectoriesCrossedNoCollision(detectionStrategy);
    checkTrajectoriesCrossedCollision(detectionStrategy);
  }
  
  def checkTrajectoriesOverlayFrontalCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 10), Vector(15, 0));
    val s2 = new Mock(0.05, Point(30, 10), Vector(15, 180));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Frontal collision not detected"));
    if(!DoubleMath.fuzzyEquals(663.33333, time, 0.00001)) {
      throw new ContactTimePredictionException("Frontal collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesOverlayFrontalNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 10), Vector(3, 0));
    val s2 = new Mock(0.05, Point(30, 10), Vector(3, 180));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      throw new UnexpectedDetectionException("Spheres moved too slow to cause frontal collision");
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 10), Vector(15, 0));
    val s2 = new Mock(0.05, Point(30, 10), Vector(15, 0));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Spheres moved unidirectionally with same velocity and must not cause collision";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 10), Vector(35, 0));
    val s2 = new Mock(0.05, Point(30, 10), Vector(15, 0));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Rear collision not detected"));
    if(!DoubleMath.fuzzyEquals(995, time, 0.00001)) {
      throw new ContactTimePredictionException("Rear collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesCrossedNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 100), Vector(50, 0));
    val s2 = new Mock(0.05, Point(60, 110), Vector(50, 270));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Trajectories are crossed, but at any point of time spheres must not collide";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesCrossedCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[Mock]) = {
    val s1 = new Mock(0.05, Point(10, 100), Vector(100, 0));
    val s2 = new Mock(0.05, Point(60, 150), Vector(100, 270));
    val detection = detectionStrategy.detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("No cross collision detected"));
    if(!DoubleMath.fuzzyEquals(499.29289, time, 0.00001)) {
      throw new ContactTimePredictionException("Crossed collision contact time prediction was incorrect");
    }
  }
}

class Mock(val radius:Double, val position:Point, val velocity:Vector)
extends Movable with Formed[Circle] {
  def setPosition(nextPosition:Point) = {}
  def setVelocity(newVelocity:Vector) = {}
  def shape = Circle(position, radius);
  def rotatable = None;
  def snapshot = this;
}