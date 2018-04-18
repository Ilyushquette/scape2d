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
import scape.scape2d.engine.geom.HalfPI
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point

class RotationalFunctionsTest {
  @Test
  def testAsRadiansPerTimestepCounterclockwise = {
    val radiansPerSecond = 0.785398; // 45 degrees
    Assert.assertEquals(0.0785398, asRadiansPerTimestep(radiansPerSecond, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerTimestepClockwise = {
    val radiansPerSecond = -1.5708; // -90 degrees
    Assert.assertEquals(-0.15708, asRadiansPerTimestep(radiansPerSecond, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondCounterclockwise = {
    val radiansPerTimestep = 0.174533; // 10 degrees
    Assert.assertEquals(1.74533, asRadiansPerSecond(radiansPerTimestep, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondClockwise = {
    val radiansPerTimestep = -3.14159; // 180 degrees
    Assert.assertEquals(-31.4159, asRadiansPerSecond(radiansPerTimestep, 100), 0.00001);
  }
  
  @Test
  def testPostRotationPositionWithoutRotatable = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(4, 4));
    Mockito.when(movableMock.rotatable).thenReturn(None);
    Assert.assertEquals(Point(4, 4), positionForTimeOf(movableMock)(1000));
  }
  
  @Test
  def testPostRotationPositionZeroAngularVelocity = {
    val rotatableMock = Mockito.mock(classOf[Rotatable]);
    Mockito.when(rotatableMock.center).thenReturn(Point.origin);
    Mockito.when(rotatableMock.angularVelocity).thenReturn(0);
    
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(0, 1));
    Mockito.when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    Assert.assertEquals(Point(0, 1), positionForTimeOf(movableMock)(1000));
  }
  
  @Test
  def testPostRotationPositionCounterclockwise = {
    val rotatableMock = Mockito.mock(classOf[Rotatable]);
    Mockito.when(rotatableMock.center).thenReturn(Point.origin);
    Mockito.when(rotatableMock.angularVelocity).thenReturn(1.5708); // 90 degrees
    
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(0, 1));
    Mockito.when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(1000);
    Assert.assertEquals(-1, postRotationPosition.x, 0.00001);
    Assert.assertEquals(0, postRotationPosition.y, 0.00001);
  }
  
  @Test
  def testPostRotationPositionClockwise = {
    val rotatableMock = Mockito.mock(classOf[Rotatable]);
    Mockito.when(rotatableMock.center).thenReturn(Point.origin);
    Mockito.when(rotatableMock.angularVelocity).thenReturn(-0.785398); // -45 degrees
    
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(1, 0));
    Mockito.when(movableMock.rotatable).thenReturn(Some(rotatableMock));
    
    val postRotationPosition = positionForTimeOf(movableMock)(1000);
    Assert.assertEquals(0.70710, postRotationPosition.x, 0.00001);
    Assert.assertEquals(-0.70710, postRotationPosition.y, 0.00001);
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(0, 3), Vector(), None);
    val movable2 = new MovableMock(Point(0, -9), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), -PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(6, ft(500), Epsilon);
  }
  
  @Test
  def testDistanceForTimeWhenMovablesOverlapZeroDistance = {
    val movable1 = new MovableMock(Point(0, 3), Vector(), None);
    val movable2 = new MovableMock(Point(0, -9), Vector(), None);
    val rotatable1 = new RotatableMock(Point.origin, PI, Set(movable1));
    val rotatable2 = new RotatableMock(Point(0, -6), -PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(0, ft(1000), Epsilon);
  }
  
  @Test
  def testDistanceForAnyTimeSameMovablesWithoutRotatables = {
    val movable1 = new MovableMock(Point(-3, 0), Vector(), None);
    val movable2 = new MovableMock(Point(3, -6), Vector(), None);
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(500), Epsilon);
    Assert.assertEquals(movable1.position distanceTo movable2.position, ft(1000), Epsilon);
  }
  
  @Test
  def testDistanceForTimeOfMovableWithNoRotatableAndMovableWithRotatable = {
    val movable1 = new MovableMock(Point(-3, 0), Vector(), None);
    val movable2 = new MovableMock(Point(3, -6), Vector(), None);
    val rotatable2 = new RotatableMock(Point(0, -6), PI, Set(movable2));
    val ft = distanceForTimeOf(movable1, movable2);
    Assert.assertEquals(Point(0, -3) distanceTo movable1.position, ft(500), Epsilon);
  }
  
  @Test
  def testAngularToLinearVelocityScalar = {
    val movable = new MovableMock(Point(-3, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, 1.57079, Set(movable));
    val linearVelocityScalar = angularToLinearVelocityScalar(movable);
    Assert.assertEquals(4.71237, linearVelocityScalar, Epsilon);
  }
  
  @Test
  def testNegativeAngularToAbsoluteLinearVelocityScalar = {
    val movable = new MovableMock(Point(-10, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, -3.14159, Set(movable));
    val linearVelocityScalar = angularToLinearVelocityScalar(movable);
    Assert.assertEquals(31.4159, linearVelocityScalar, Epsilon);
  }
  
  @Test
  def testAngularToLinearVelocity = {
    val movable = new MovableMock(Point(-3, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, 1.57079, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(4.71237, 4.7123889803), linearVelocity);
  }
  
  @Test
  def testNegativeAngularToInvertedLinearVelocity = {
    val movable = new MovableMock(Point(-10, 0), Vector(), None);
    val rotatable = new RotatableMock(Point.origin, -3.14159, Set(movable));
    val linearVelocity = angularToLinearVelocity(movable);
    Assert.assertEquals(Vector(31.4159, HalfPI), linearVelocity);
  }
}