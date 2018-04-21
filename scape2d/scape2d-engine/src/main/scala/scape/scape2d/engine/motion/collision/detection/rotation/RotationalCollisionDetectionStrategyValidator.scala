package scape.scape2d.engine.motion.collision.detection.rotation

import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.geom.TwicePI
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.collision.detection.UnexpectedDetectionException
import scape.scape2d.engine.core.RotatableMock
import com.google.common.math.DoubleMath
import scape.scape2d.engine.motion.collision.detection.NoDetectionException
import scape.scape2d.engine.motion.collision.detection.ContactTimePredictionException
import scape.scape2d.engine.motion.rotational.distanceForTimeOf
import org.apache.log4j.Logger

object RotationalCollisionDetectionStrategyValidator {
  private val log = Logger.getLogger(getClass);
  
  def check(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    log.info("Validating rotational collision detection strategy " + detectionStrategy);
    checkNonRotatablesNoCollision(detectionStrategy);
    checkTrajectoriesDontIntersectNoCollision(detectionStrategy);
    checkTurnRoundEachOtherNoCollision(detectionStrategy);
    checkDirectCollision(detectionStrategy);
    checkSlowVersusTwiceAsFastNoCollision(detectionStrategy);
    checkLargeFastRotatableWithSmallNonRotatableCollision(detectionStrategy);
    checkSmallSlowRotatableWithLargeNonRotatableCollision(detectionStrategy);
    log.info(detectionStrategy + " rotational collision detection strategy successfully passed validation!");
  }
  
  def checkNonRotatablesNoCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point.origin, 1), Vector(), None);
    val movable2 = new MovableMock(Circle(Point(0, 10), 1), Vector(), None);
    val detection = detectionStrategy.detect(movable1, movable2, 10000);
    if(detection.isDefined)
      throw UnexpectedDetectionException();
  }
  
  /**
   * CASE 1 https://github.com/Ilyushquette/scape2d/issues/18
   */
  def checkTrajectoriesDontIntersectNoCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(93, -60), 13), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, 2.51327, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(164, 212), 13), Vector(), None);
    val rotatable2 = new RotatableMock(Point(230, 140), 2.74017, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined)
      throw UnexpectedDetectionException();
  }
  
  /**
   * CASE 2 https://github.com/Ilyushquette/scape2d/issues/18
   */
  def checkTurnRoundEachOtherNoCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(93, -60), 13), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, 2.51327, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(60, 156), 13), Vector(), None);
    val rotatable2 = new RotatableMock(Point(126, 83), 2.74017, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined)
      throw UnexpectedDetectionException();
  }
  
  /**
   * CASE 3 https://github.com/Ilyushquette/scape2d/issues/18
   */
  def checkDirectCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(93, -60), 13), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, 2.51327, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(102, 224), 13), Vector(), None);
    val rotatable2 = new RotatableMock(Point(168, 151), 2.74017, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = 511;
    if(time > contactTime || time < contactTime - 50)
      throw ContactTimePredictionException();
  }
  
  /**
   * CASE 4 https://github.com/Ilyushquette/scape2d/issues/18
   */
  def checkSlowVersusTwiceAsFastNoCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(93, -60), 13), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, 5.88176, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(102, 224), 13), Vector(), None);
    val rotatable2 = new RotatableMock(Point(168, 151), 2.74017, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 1000);
    if(detection.isDefined)
      throw UnexpectedDetectionException();
  }
  
  def checkLargeFastRotatableWithSmallNonRotatableCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(-10, 0), 5), Vector(), None);
    val movable2 = new MovableMock(Circle(Point(-8.48528, 8.48528), 0.1), Vector(), None);
    val angularVelocity = -(TwicePI * 10); // 10 revs per second clockwise | 0.0628 rad per millisecond
    val rotatable1 = new RotatableMock(Point.origin, angularVelocity, Set(movable1));
    val detection = detectionStrategy.detect(movable1, movable2, 100);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = 5.63;
    if(time > contactTime || time < contactTime - 0.2)
      throw ContactTimePredictionException();
  }
  
  def checkSmallSlowRotatableWithLargeNonRotatableCollision(detectionStrategy:RotationalCollisionDetectionStrategy[MovableMock]) = {
    val movable1 = new MovableMock(Circle(Point(-10, 0), 5), Vector(), None);
    val movable2 = new MovableMock(Circle(Point(-8.48528, 8.48528), 0.1), Vector(), None);
    val angularVelocity = TwicePI * 20; // 20 revs per second clockwise | 0.1256 rad per millisecond
    val rotatable2 = new RotatableMock(Point.origin, angularVelocity, Set(movable2));
    val detection = detectionStrategy.detect(movable1, movable2, 100);
    val time = detection.getOrElse(throw NoDetectionException());
    val contactTime = 2.815;
    if(time > contactTime || time < contactTime - 0.2)
      throw ContactTimePredictionException();
  }
}