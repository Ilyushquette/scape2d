package scape.scape2d.engine.motion.collision

import com.google.common.math.DoubleMath

import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Spherical
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.Movable

class DetectionStrategyValidator {
  type CollisionDetector = (Movable with Spherical, Movable with Spherical, Double) => Option[Double];
  
  def checkTrajectoriesOverlayFrontalCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 10), new Vector2D(15, 0));
    val s2 = new MovableSphere(5, new Point2D(30, 10), new Vector2D(15, 180));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Frontal collision not detected"));
    if(!DoubleMath.fuzzyEquals(0.33333, time, 0.00001)) {
      throw new ContactTimePredictionException("Frontal collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesOverlayFrontalNoCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 10), new Vector2D(3, 0));
    val s2 = new MovableSphere(5, new Point2D(30, 10), new Vector2D(3, 180));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      throw new UnexpectedDetectionException("Spheres moved too slow to cause frontal collision");
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalNoCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 10), new Vector2D(15, 0));
    val s2 = new MovableSphere(5, new Point2D(30, 10), new Vector2D(15, 0));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Spheres moved unidirectionally with same velocity and must not cause collision";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 10), new Vector2D(35, 0));
    val s2 = new MovableSphere(5, new Point2D(30, 10), new Vector2D(15, 0));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Rear collision not detected"));
    if(!DoubleMath.fuzzyEquals(0.5, time, 0.00001)) {
      throw new ContactTimePredictionException("Rear collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesCrossedNoCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 100), new Vector2D(50, 0));
    val s2 = new MovableSphere(5, new Point2D(60, 110), new Vector2D(50, 270));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Trajectories are crossed, but at any point of time spheres must not collide";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesCrossedCollision(detect:CollisionDetector) = {
    val s1 = new MovableSphere(5, new Point2D(10, 100), new Vector2D(100, 0));
    val s2 = new MovableSphere(5, new Point2D(60, 150), new Vector2D(100, 270));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("No cross collision detected"));
    if(!DoubleMath.fuzzyEquals(0.42928, time, 0.00001)) {
      throw new ContactTimePredictionException("Crossed collision contact time prediction was incorrect");
    }
  }
}

class MovableSphere(val radius:Double, val position:Point2D, val velocity:Vector2D)
extends Movable with Spherical;