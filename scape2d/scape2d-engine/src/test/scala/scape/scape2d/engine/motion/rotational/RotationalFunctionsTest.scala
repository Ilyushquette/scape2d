package scape.scape2d.engine.motion.rotational

import java.lang.Math.PI

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.when

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Minute
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.geom.shape.Polygon
import scape.scape2d.engine.geom.shape.Shape

class RotationalFunctionsTest {
  @Test
  def testAngularDisplacementWithoutRotatable = {
    val movableMock = new MovableMock(Point.origin, Velocity.zero, None);
    Assert.assertEquals(Angle.zero.unbound, angularDisplacementForTimeOf(movableMock)(Minute));
  }
  
  @Test
  def testAngularDisplacementZeroAngularVelocity = {
    val movableMock = new MovableMock(Point(4, 4), Velocity.zero, None);
    val rotatableMock = new RotatableMock(Point.origin, AngularVelocity.zero, Set(movableMock));
    Assert.assertEquals(Angle.zero.unbound, angularDisplacementForTimeOf(movableMock)(Duration(5, Second)));
  }
  
  @Test
  def testAngularDisplacementCounterclockwise = {
    val movableMock = new MovableMock(Point(4, 4), Vector(35, 90(Degree)) / Second, None);
    val rotatableMock = new RotatableMock(Point.origin, Angle.right / Second, Set(movableMock));
    Assert.assertEquals(UnboundAngle(450, Degree), angularDisplacementForTimeOf(movableMock)(Duration(5, Second)));
  }
  
  @Test
  def testAngularDisplacementClockwise = {
    val movableMock = new MovableMock(Point(4, 4), Vector(35, 90(Degree)) / Second, None);
    val rotatableMock = new RotatableMock(Point.origin, UnboundAngle(-900, Degree) / Second, Set(movableMock));
    Assert.assertEquals(UnboundAngle(-450, Degree), angularDisplacementForTimeOf(movableMock)(Duration(500, Millisecond)));
  }
  
  @Test
  def testPostRotationPositionWithoutRotatable = {
    val movableMock = mock(classOf[Movable[_ <: Shape]]);
    when(movableMock.position).thenReturn(Point(4, 4));
    when(movableMock.rotatable).thenReturn(None);
    Assert.assertEquals(Point(4, 4), positionForTimeOf(movableMock)(Second));
  }
  
  @Test
  def testPostRotationPositionZeroAngularVelocity = {
    val rotatableMock = mock(classOf[Rotatable]);
    when(rotatableMock.center).thenReturn(Point.origin);
    when(rotatableMock.angularVelocity).thenReturn(AngularVelocity.zero);
    
    val movableMock = mock(classOf[Movable[_ <: Shape]]);
    when(movableMock.position).thenReturn(Point(0, 1));
    when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    Assert.assertEquals(Point(0, 1), positionForTimeOf(movableMock)(Second));
  }
  
  @Test
  def testPostRotationPositionCounterclockwise = {
    val movableMock = new MovableMock(Point(0, 1), Velocity.zero, None);
    val rotatableMock = new RotatableMock(Point.origin, Angle.right / Second, Set(movableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(Second);
    Assert.assertEquals(Point(-1, 0), postRotationPosition);
  }
  
  @Test
  def testPostRotationPositionClockwise = {
    val movableMock = new MovableMock(Point(1, 0), Velocity.zero, None);
    val rotatableMock = new RotatableMock(Point.origin, UnboundAngle(-45, Degree) / Second, Set(movableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(Second);
    Assert.assertEquals(Point(0.7071067811, -0.7071067811), postRotationPosition);
  }
  
  @Test
  def testPostRotationShapeWithoutRotatable = {
    val movableMock = new MovableMock(Circle(Point(34.3, 34.4), 4.4), Vector(1, 135(Degree)) / Second, None);
    Assert.assertEquals(Circle(Point(34.3, 34.4), 4.4), rotatedShapeForTimeOf(movableMock)(Minute));
  }
  
  @Test
  def testPostRotationShapeZeroAngularVelocity = {
    val movableMock = new MovableMock(Circle(Point(-3, -3), 1), Velocity.zero, None);
    val rotatableMock = new RotatableMock(Point.origin, AngularVelocity.zero, Set(movableMock));
    Assert.assertEquals(Circle(Point(-3, -3), 1), rotatedShapeForTimeOf(movableMock)(Second));
  }
  
  /**
   * Rectangle rotation around the fixed point:
   * https://www.desmos.com/calculator/v4r5smbbpv
   */
  @Test
  def testPostRotationShapeCounterClockwise = {
    val movableMock = mock(classOf[Movable[Polygon]]);
    when(movableMock.shape).thenReturn(AxisAlignedRectangle(Point(-2, 2), 4, 4));
    when(movableMock.isRotating).thenReturn(true);
    val rotatableMock = mock(classOf[Rotatable]);
    when(rotatableMock.center).thenReturn(Point.origin);
    when(rotatableMock.angularVelocity).thenReturn(Angle.right / Second);
    when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val rotatedPolygon = rotatedShapeForTimeOf(movableMock)(Duration(500, Millisecond));
    val expectedPolygon = PolygonBuilder(Point(-2.8284271247, 0),
                                         Point(-5.6568542494, 2.8284271247),
                                         Point(-2.8284271247, 5.6568542494))
                                        .to(Point(0, 2.8284271247)).build;
    Assert.assertEquals(expectedPolygon, rotatedPolygon);
  }
  
  @Test
  def testPostRotationShapeClockwise = {
    val movableMock = mock(classOf[Movable[Segment]]);
    when(movableMock.shape).thenReturn(Segment(Point(0, 1), Point(0, -1)));
    when(movableMock.isRotating).thenReturn(true);
    val rotatableMock = mock(classOf[Rotatable]);
    when(rotatableMock.center).thenReturn(Point.origin);
    when(rotatableMock.angularVelocity).thenReturn(UnboundAngle(-270, Degree) / Second);
    when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val rotatedSegment = rotatedShapeForTimeOf(movableMock)(Duration(3, Second));
    val expectedSegment = Segment(Point(-1, 0), Point(1, 0));
    Assert.assertEquals(expectedSegment, rotatedSegment);
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(0, 3), Velocity.zero, None);
    val movable2 = new MovableMock(Point(0, -9), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, Angle.straight / Second, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), UnboundAngle(-PI, Radian) / Second, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(6, ft(Duration(500, Millisecond)), Epsilon);
  }
  
  @Test
  def testDistanceForTimeWhenMovablesOverlapZeroDistance = {
    val movable1 = new MovableMock(Point(0, 3), Velocity.zero, None);
    val movable2 = new MovableMock(Point(0, -9), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, Angle.straight / Second, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), UnboundAngle(-PI, Radian) / Second, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(0, ft(Second), Epsilon);
  }
  
  @Test
  def testDistanceForAnyTimeSameMovablesWithoutRotatables = {
    val movable1 = new MovableMock(Point(-3, 0), Velocity.zero, None);
    val movable2 = new MovableMock(Point(3, -6), Velocity.zero, None);
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(Duration(500, Millisecond)), Epsilon);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(Second), Epsilon);
  }
  
  @Test
  def testDistanceForTimeOfMovableWithNoRotatableAndMovableWithRotatable = {
    val movable1 = new MovableMock(Point(-3, 0), Velocity.zero, None);
    val movable2 = new MovableMock(Point(3, -6), Velocity.zero, None);
    val rotatable2 = new RotatableMock(Point(0, -6), Angle.straight / Second, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(Point(0, -3) distanceTo movable1.position, ft(Duration(500, Millisecond)), Epsilon);
  }
  
  @Test
  def testAngularToLinearVelocity = {
    val movable = new MovableMock(Point(-3, 0), Velocity.zero, None);
    val rotatable = new RotatableMock(Point.origin, Angle.right / Second, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(4.7123889803, 270(Degree)) / Second, linearVelocity);
  }
  
  @Test
  def testNegativeAngularToInvertedLinearVelocity = {
    val movable = new MovableMock(Point(-10, 0), Velocity.zero, None);
    val rotatable = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(31.4159265358, 90(Degree)) / Second, linearVelocity);
  }
}