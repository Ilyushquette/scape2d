package scape.scape2d.engine.motion

import java.lang.Math.PI

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.when

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.doubleToTime
import scape.scape2d.engine.geom.shape.Polygon

class MotionFunctionsTest {
  @Test
  def testPositionForTime = {
    val movable = new MovableMock(Point(-1, 0), Vector.from(Components(3, 1)) / Second, None);
    val rotatable = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable));
    Assert.assertEquals(Point(4, 1), positionForTimeOf(movable)(Second));
  }
  
  @Test
  def testPositionForNegativeTime = {
    val movable = new MovableMock(Point(0, -1), Vector.from(Components(-6, 0)) / Second, None);
    val rotatable = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable));
    Assert.assertEquals(Point(4, 0), positionForTimeOf(movable)(Duration(-500, Millisecond)));
  }
  
  /**
   * Rectangle rotation and linear motion:
   * https://www.desmos.com/calculator/nv4gylij0h
   */
  @Test
  def testShapeForTime = {
    val movableMock = mock(classOf[Movable[Polygon]]);
    when(movableMock.shape).thenReturn(AxisAlignedRectangle(Point(-2, 2), 4, 4));
    when(movableMock.velocity).thenReturn(Vector(8, Angle.zero) / Second);
    when(movableMock.isMovingLinearly).thenReturn(true);
    when(movableMock.isRotating).thenReturn(true);
    val rotatableMock = mock(classOf[Rotatable]);
    when(rotatableMock.center).thenReturn(Point.origin);
    when(rotatableMock.angularVelocity).thenReturn(UnboundAngle(-90, Degree) / Second);
    when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val movedPolygon = shapeForTimeOf(movableMock)(Duration(800, Millisecond));
    val expectedPolygon = PolygonBuilder(Point(7.6840790438, 2.5201470213),
                                         Point(11.4883051090, 3.7562149988),
                                         Point(12.7243730865, -0.0480110663))
                                        .to(Point(8.9201470213, -1.2840790438)).build;
    Assert.assertEquals(expectedPolygon, movedPolygon);
  }
  
  @Test
  def testShapeForNegativeTime = {
    val movableMock = mock(classOf[Movable[Segment]]);
    when(movableMock.shape).thenReturn(Segment(Point(-1, 0), Point(3, 0)));
    when(movableMock.velocity).thenReturn(Vector(5, Angle.right) / Second);
    when(movableMock.isMovingLinearly).thenReturn(true);
    when(movableMock.isRotating).thenReturn(true);
    val rotatableMock = mock(classOf[Rotatable]);
    when(rotatableMock.center).thenReturn(Point.origin);
    when(rotatableMock.angularVelocity).thenReturn(UnboundAngle(-PI, Radian) / Second);
    when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val movedSegment = shapeForTimeOf(movableMock)(Duration(-500, Millisecond));
    Assert.assertEquals(Segment(Point(0, -3.5), Point(0, 0.5)), movedSegment);
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(-1, 0), Vector.from(Components(3, 0)) / Second, None);
    val rotatable1 = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable1));
    val movable2 = new MovableMock(Point(11, 0), Vector.from(Components(-3, 0)) / Second, None);
    val rotatable2 = new RotatableMock(Point(10, 0), Angle.straight / Second, Set(movable2));
    Assert.assertEquals(7, distanceForTimeOf(movable1, movable2)(500(Millisecond)), Epsilon);
  }
}