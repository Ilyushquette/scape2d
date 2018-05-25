package scape.scape2d.engine.motion.rotational

import java.lang.Math.PI
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second

class RotationalFunctionsTest {
  @Test
  def testAsRadiansPerTimestepCounterclockwise = {
    val radiansPerSecond = 0.785398; // 45 degrees
    Assert.assertEquals(0.0785398, asRadiansPerTimestep(radiansPerSecond, Duration(100, Millisecond)), 0.00001);
  }
  
  @Test
  def testAsRadiansPerTimestepClockwise = {
    val radiansPerSecond = -1.5708; // -90 degrees
    Assert.assertEquals(-0.15708, asRadiansPerTimestep(radiansPerSecond, Duration(100, Millisecond)), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondCounterclockwise = {
    val radiansPerTimestep = 0.174533; // 10 degrees
    Assert.assertEquals(1.74533, asRadiansPerSecond(radiansPerTimestep, Duration(100, Millisecond)), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondClockwise = {
    val radiansPerTimestep = -3.14159; // 180 degrees
    Assert.assertEquals(-31.4159, asRadiansPerSecond(radiansPerTimestep, Duration(100, Millisecond)), 0.00001);
  }
  
  @Test
  def testPostRotationPositionWithoutRotatable = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(4, 4));
    Mockito.when(movableMock.rotatable).thenReturn(None);
    Assert.assertEquals(Point(4, 4), positionForTimeOf(movableMock)(Second));
  }
  
  @Test
  def testPostRotationPositionZeroAngularVelocity = {
    val rotatableMock = Mockito.mock(classOf[Rotatable]);
    Mockito.when(rotatableMock.center).thenReturn(Point.origin);
    Mockito.when(rotatableMock.angularVelocity).thenReturn(0);
    
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(0, 1));
    Mockito.when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    Assert.assertEquals(Point(0, 1), positionForTimeOf(movableMock)(Second));
  }
  
  @Test
  def testPostRotationPositionCounterclockwise = {
    val movableMock = new MovableMock(Point(0, 1), Vector(), None);
    val rotatableMock = new RotatableMock(Point.origin, PI / 2, Set(movableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(Second);
    Assert.assertEquals(Point(-1, 0), postRotationPosition);
  }
  
  @Test
  def testPostRotationPositionClockwise = {
    val movableMock = new MovableMock(Point(1, 0), Vector(), None);
    val rotatableMock = new RotatableMock(Point.origin, -PI / 4, Set(movableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(Second);
    Assert.assertEquals(Point(0.7071067811, -0.7071067811), postRotationPosition);
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(0, 3), Vector(), None);
    val movable2 = new MovableMock(Point(0, -9), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), -PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(6, ft(Duration(500, Millisecond)), Epsilon);
  }
  
  @Test
  def testDistanceForTimeWhenMovablesOverlapZeroDistance = {
    val movable1 = new MovableMock(Point(0, 3), Vector(), None);
    val movable2 = new MovableMock(Point(0, -9), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), -PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(0, ft(Second), Epsilon);
  }
  
  @Test
  def testDistanceForAnyTimeSameMovablesWithoutRotatables = {
    val movable1 = new MovableMock(Point(-3, 0), Vector(), None);
    val movable2 = new MovableMock(Point(3, -6), Vector(), None);
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(Duration(500, Millisecond)), Epsilon);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(Second), Epsilon);
  }
  
  @Test
  def testDistanceForTimeOfMovableWithNoRotatableAndMovableWithRotatable = {
    val movable1 = new MovableMock(Point(-3, 0), Vector(), None);
    val movable2 = new MovableMock(Point(3, -6), Vector(), None);
    val rotatable2 = new RotatableMock(Point(0, -6), PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(Point(0, -3) distanceTo movable1.position, ft(Duration(500, Millisecond)), Epsilon);
  }
  
  @Test
  def testAngularToLinearVelocityScalar = {
    val movable = new MovableMock(Point(-3, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, PI / 2, Set(movable));
    val linearVelocityScalar = angularToLinearVelocityScalar(movable);
    Assert.assertEquals(4.7123889803, linearVelocityScalar, Epsilon);
  }
  
  @Test
  def testNegativeAngularToAbsoluteLinearVelocityScalar = {
    val movable = new MovableMock(Point(-10, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, -PI, Set(movable));
    val linearVelocityScalar = angularToLinearVelocityScalar(movable);
    Assert.assertEquals(31.4159265358, linearVelocityScalar, Epsilon);
  }
  
  @Test
  def testAngularToLinearVelocity = {
    val movable = new MovableMock(Point(-3, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, PI / 2, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(4.7123889803, 270(Degree)), linearVelocity);
  }
  
  @Test
  def testNegativeAngularToInvertedLinearVelocity = {
    val movable = new MovableMock(Point(-10, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, -PI, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(31.4159265358, 90(Degree)), linearVelocity);
  }
}