package scape.scape2d.engine.motion.collision.detection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.core.mock.MovableRotatableMock
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.ConvexPolygonBuilder
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.ContactContainer
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity
import scape.scape2d.engine.motion.shapeForTimeOf
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class BinarySearchDiscreteConvexesContactDetectionStrategyTest {
  @Test
  def testNoCollisionNoContact = {
    val detectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy();
    val movable1 = new MovableRotatableMock(
        shape = Circle(Point.origin, 4),
        velocity = Vector(10, Angle.right) / Second,
        angularVelocity = AngularVelocity.zero
    );
    val movable2 = new MovableRotatableMock(
        shape = Circle(Point(0, 10), 5),
        velocity = Vector(10, Angle.right) / Second,
        angularVelocity = Angle.full / Second
    );
    movable1.shape = shapeForTimeOf(movable1)(Second);
    movable2.shape = shapeForTimeOf(movable2)(Second);
    val detection = detectionStrategy.detect(movable1, movable2, Second);
    Assert.assertTrue(detection.isEmpty);
  }
  
  @Test
  def testContactDetectedWithoutBinarySearching = {
    val detectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy();
    val movable = new MovableRotatableMock(
        shape = AxisAlignedRectangle(Point.origin, 4, 4),
        velocity = Velocity.zero,
        angularVelocity = AngularVelocity.zero
    );
    val stationary = new MovableRotatableMock(
        shape = AxisAlignedRectangle(Point(5, 5), 3, 3),
        velocity = Vector.from(Components(-2, -2)) / Second,
        angularVelocity = AngularVelocity.zero
    );
    movable.shape = shapeForTimeOf(movable)(Second);
    stationary.shape = shapeForTimeOf(stationary)(Second);
    val detection = detectionStrategy.detect(movable, stationary, Second);
    val expectedDetection = ContactContainer(
        shape1 = AxisAlignedRectangle(Point.origin, 4, 4),
        shape2 = AxisAlignedRectangle(Point(3, 3), 3, 3),
        contacts = List(Contact(Point(3.5, 3.5), 45(Degree))),
        time = Second
    );
    Assert.assertEquals(Some(expectedDetection), detection);
  }
  
  @Test
  def testContactDetectedOnFirstTimeSplitDuringBinarySearch = {
    val detectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy();
    val movableRhombus = new MovableRotatableMock(
        shape = ConvexPolygonBuilder(Point(0, 11), Point(3, 14), Point(6, 11)).to(Point(3, 8)).build(),
        velocity = Vector(8, 270(Degree)) / Second,
        angularVelocity = AngularVelocity.zero
    );
    val rotatingRhombus = new MovableRotatableMock(
        shape = ConvexPolygonBuilder(Point(0, 3), Point(3, 6), Point(6, 3)).to(Point(3, 0)).build(),
        velocity = Velocity.zero,
        angularVelocity = Angle.straight / Second
    );
    movableRhombus.shape = shapeForTimeOf(movableRhombus)(Second);
    rotatingRhombus.shape = shapeForTimeOf(rotatingRhombus)(Second);
    val detection = detectionStrategy.detect(movableRhombus, rotatingRhombus, Second);
    val expectedDetection = ContactContainer(
        shape1 = ConvexPolygonBuilder(Point(0, 7), Point(3, 10), Point(6, 7)).to(Point(3, 4)).build(),
        shape2 = ConvexPolygonBuilder(Point(3, 0), Point(0, 3), Point(3, 6)).to(Point(6, 3)).build(),
        contacts = List(Contact(Point(3, 5), 270(Degree))),
        time = Duration(500, Millisecond)
    );
    Assert.assertEquals(Some(expectedDetection), detection);
  }
  
  @Test
  def testContactDetectedOnSecondTimeSplitDuringBinarySearch = {
    val detectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy();
    val movingRectangle = new MovableRotatableMock(
        shape = AxisAlignedRectangle(Point(0.5, 1), 2, 2),
        velocity = Vector(6, Angle.zero) / Second,
        angularVelocity = AngularVelocity.zero
    );
    val rotatingRectangle = new MovableRotatableMock(
        shape = AxisAlignedRectangle(Point(4, 0), 4, 4),
        velocity = Velocity.zero,
        angularVelocity = Angle.full / Second
    );
    movingRectangle.shape = shapeForTimeOf(movingRectangle)(Second);
    rotatingRectangle.shape = shapeForTimeOf(rotatingRectangle)(Second);
    val detection = detectionStrategy.detect(movingRectangle, rotatingRectangle, Second);
    val expectedDetection = ContactContainer(
        shape1 = AxisAlignedRectangle(Point(2, 1), 2, 2),
        shape2 = AxisAlignedRectangle(Point(4, 0), 4, 4),
        contacts = List(Contact(Point(4, 2), Angle.zero)),
        time = Duration(250, Millisecond)
    );
    Assert.assertEquals(Some(expectedDetection), detection);
  }
}