package scape.scape2d.engine.motion.collision.detection

import com.google.common.math.DoubleMath
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

object DetectionStrategyValidator {
  def check(detect:DetectionStrategy) = {
    checkTrajectoriesOverlayFrontalCollision(detect);
    checkTrajectoriesOverlayFrontalNoCollision(detect);
    checkTrajectoriesOverlayUnidirectionalNoCollision(detect);
    checkTrajectoriesOverlayUnidirectionalCollision(detect);
    checkTrajectoriesCrossedNoCollision(detect);
    checkTrajectoriesCrossedCollision(detect);
  }
  
  def checkTrajectoriesOverlayFrontalCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 10), new Vector2D(15, 0));
    val s2 = new Mock(0.05, Point(30, 10), new Vector2D(15, 180));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Frontal collision not detected"));
    if(!DoubleMath.fuzzyEquals(663.33333, time, 0.00001)) {
      throw new ContactTimePredictionException("Frontal collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesOverlayFrontalNoCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 10), new Vector2D(3, 0));
    val s2 = new Mock(0.05, Point(30, 10), new Vector2D(3, 180));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      throw new UnexpectedDetectionException("Spheres moved too slow to cause frontal collision");
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalNoCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 10), new Vector2D(15, 0));
    val s2 = new Mock(0.05, Point(30, 10), new Vector2D(15, 0));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Spheres moved unidirectionally with same velocity and must not cause collision";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 10), new Vector2D(35, 0));
    val s2 = new Mock(0.05, Point(30, 10), new Vector2D(15, 0));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("Rear collision not detected"));
    if(!DoubleMath.fuzzyEquals(995, time, 0.00001)) {
      throw new ContactTimePredictionException("Rear collision contact time prediction was incorrect");
    }
  }
  
  def checkTrajectoriesCrossedNoCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 100), new Vector2D(50, 0));
    val s2 = new Mock(0.05, Point(60, 110), new Vector2D(50, 270));
    val detection = detect(s1, s2, 1000);
    if(!detection.isEmpty) {
      val error = "Trajectories are crossed, but at any point of time spheres must not collide";
      throw new UnexpectedDetectionException(error);
    }
  }
  
  def checkTrajectoriesCrossedCollision(detect:DetectionStrategy) = {
    val s1 = new Mock(0.05, Point(10, 100), new Vector2D(100, 0));
    val s2 = new Mock(0.05, Point(60, 150), new Vector2D(100, 270));
    val detection = detect(s1, s2, 1000);
    val time = detection.getOrElse(throw new NoDetectionException("No cross collision detected"));
    if(!DoubleMath.fuzzyEquals(499.29289, time, 0.00001)) {
      throw new ContactTimePredictionException("Crossed collision contact time prediction was incorrect");
    }
  }
}

private class Mock(val radius:Double, val position:Point, val velocity:Vector2D)
extends Movable with Formed[Circle] {
  def setPosition(nextPosition:Point) = {}
  def setVelocity(newVelocity:Vector2D) = {}
  def shape = Circle(position, radius);
  def rotatable = None;
  def snapshot = this;
}