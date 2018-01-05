package scape.scape2d.engine.motion.rotational

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.Rotatable

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
}