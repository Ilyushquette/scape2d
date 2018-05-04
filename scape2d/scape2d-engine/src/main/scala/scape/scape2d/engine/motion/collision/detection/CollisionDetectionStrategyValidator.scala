package scape.scape2d.engine.motion.collision.detection

import java.lang.Math.PI
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.TwicePI
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetectionStrategyValidator
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategyValidator

object CollisionDetectionStrategyValidator {
  def check(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    LinearMotionCollisionDetectionStrategyValidator.check(detectionStrategy);
    RotationalCollisionDetectionStrategyValidator.check(detectionStrategy);
    checkStationaryNoCollision(detectionStrategy);
    checkSynchronousPairNoCollision(detectionStrategy);
    checkDirectCollision(detectionStrategy);
    checkFastVersusSlowNoCollision(detectionStrategy);
    checkSmallFastVersusLargeSlowCollision(detectionStrategy);
  }
  
  def checkStationaryNoCollision(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(0, -1), 1), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, 0, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(10, -1), 1), Vector(), None);
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined) throw UnexpectedDetectionException();
  }
  
  def checkSynchronousPairNoCollision(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(0, -1), 0.1), Vector(4, 90), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val movable2 = new MovableMock(Circle(Point.origin, 0.1), Vector(4, 90), None);
    val rotatable2 = new RotatableMock(Point(0, 1), PI, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined) throw UnexpectedDetectionException();
  }
  
  def checkDirectCollision(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(0, -1), 1), Vector(4, 90), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(0, 11), 1), Vector(4, 270), None);
    val rotatable2 = new RotatableMock(Point(0, 10), -PI, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = 798.42135;
    if(time > contactTime || time < contactTime - 25)
      throw ContactTimePredictionException();
  }
  
  def checkFastVersusSlowNoCollision(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(0, -1), 0.1), Vector(4, 90), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(0, 11), 0.1), Vector(4, 270), None);
    val rotatable2 = new RotatableMock(Point(0, 10), -TwicePI, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined) throw UnexpectedDetectionException();
  }
  
  def checkSmallFastVersusLargeSlowCollision(detectionStrategy:CollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(0, -1), 0.1), Vector(4, 90), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(0, 11), 1.9), Vector(4, 270), None);
    val rotatable2 = new RotatableMock(Point(0, 10), -TwicePI, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = 999.99171;
    if(time > contactTime || time < contactTime - 17)
      throw ContactTimePredictionException();
  }
}