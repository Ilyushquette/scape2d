package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.when

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class LinearMotionFunctionsTest {
  @Test
  def testLinearDisplacementZero = {
    val movableMock = new MovableMock(Point.origin, Velocity.zero, None);
    Assert.assertEquals(Vector.zero, displacementForTimeOf(movableMock)(Duration(1, Second)));
  }
  
  @Test
  def testLinearDisplacement = {
    val movableMock = new MovableMock(Point(3, 5), Vector.from(Components(0, 6)) / Second, None);
    Assert.assertEquals(Vector(3, 90(Degree)), displacementForTimeOf(movableMock)(Duration(500, Millisecond)));
  }
  
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = new MovableMock(Point(3, 5), Velocity.zero, None);
    Assert.assertEquals(Point(3, 5), positionForTimeOf(movableMock)(Duration(100, Millisecond)));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = new MovableMock(Point(3, 5), Vector.from(Components(-3, -5)) / Second, None);
    Assert.assertEquals(Point(2.7, 4.5), positionForTimeOf(movableMock)(Duration(100, Millisecond)))
  }
  
  @Test
  def testPostLinearMotionShape = {
    val movableMock = mock(classOf[Movable[Circle]]);
    when(movableMock.shape).thenReturn(Circle(Point.origin, 2));
    when(movableMock.velocity).thenReturn(Vector.from(Components(-10, 10)) / Second);
    when(movableMock.isMovingLinearly).thenReturn(true);
    Assert.assertEquals(Circle(Point(-5, 5), 2), displacedShapeForTimeOf(movableMock)(Duration(500, Millisecond)));
  }
}