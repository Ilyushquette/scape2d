package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.mock.MovableMock
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.collision.detection.ContactTimePredictionException
import scape.scape2d.engine.motion.collision.detection.NoDetectionException
import scape.scape2d.engine.motion.collision.detection.UnexpectedDetectionException
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

object LinearMotionCollisionDetectionStrategyValidator {
  def check(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    checkTrajectoriesOverlayFrontalCollision(detectionStrategy);
    checkTrajectoriesOverlayFrontalNoCollision(detectionStrategy);
    checkTrajectoriesOverlayUnidirectionalNoCollision(detectionStrategy);
    checkTrajectoriesOverlayUnidirectionalCollision(detectionStrategy);
    checkTrajectoriesCrossedNoCollision(detectionStrategy);
    checkTrajectoriesCrossedCollision(detectionStrategy);
  }
  
  def checkTrajectoriesOverlayFrontalCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 10), 0.05), Vector(15, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(30, 10), 0.05), Vector(15, 180(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = Duration(663.333333333333333, Millisecond);
    if(time > contactTime || time < contactTime - Duration(17, Millisecond)) {
      throw ContactTimePredictionException();
    }
  }
  
  def checkTrajectoriesOverlayFrontalNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 10), 0.05), Vector(3, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(30, 10), 0.05), Vector(3, 180(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    if(!detection.isEmpty) {
      throw UnexpectedDetectionException();
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 10), 0.05), Vector(15, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(30, 10), 0.05), Vector(15, 0(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    if(!detection.isEmpty) {
      val error = "Spheres moved unidirectionally with same velocity and must not cause collision";
      throw UnexpectedDetectionException();
    }
  }
  
  def checkTrajectoriesOverlayUnidirectionalCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 10), 0.05), Vector(35, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(30, 10), 0.05), Vector(15, 0(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = Duration(995, Millisecond);
    if(time > contactTime || time < contactTime - Duration(17, Millisecond)) {
      throw ContactTimePredictionException();
    }
  }
  
  def checkTrajectoriesCrossedNoCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 100), 0.05), Vector(50, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(60, 110), 0.05), Vector(50, 270(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    if(!detection.isEmpty) {
      val error = "Trajectories are crossed, but at any point of time spheres must not collide";
      throw UnexpectedDetectionException();
    }
  }
  
  def checkTrajectoriesCrossedCollision(detectionStrategy:LinearMotionCollisionDetectionStrategy[MovableMock]) = {
    val s1 = new MovableMock(Circle(Point(10, 100), 0.05), Vector(100, 0(Degree)) / Second, None);
    val s2 = new MovableMock(Circle(Point(60, 150), 0.05), Vector(100, 270(Degree)) / Second, None);
    val detection = detectionStrategy.detect(s1, s2, Second);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = Duration(499.2928932188, Millisecond);
    if(time > contactTime || time < contactTime - Duration(17, Millisecond)) {
      throw ContactTimePredictionException();
    }
  }
}