package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond

class LinearMotionFunctionsTest {
  @Test
  def testAsMetersPerTimestep = {
    val metersPerSecond = Vector(1, 45(Degree));
    Assert.assertEquals(Vector(0.1, 45(Degree)), asMetersPerTimestep(metersPerSecond, Duration(100, Millisecond)));
  }
  
  @Test
  def testAsMetersPerSecond = {
    val metersPerTimestep = Vector(0.5, 270(Degree));
    Assert.assertEquals(Vector(5, 270(Degree)), asMetersPerSecond(metersPerTimestep, Duration(100, Millisecond)));
  }
  
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector(0, 47(Degree)));
    Assert.assertEquals(Point(3, 5), positionForTimeOf(movableMock)(Duration(100, Millisecond)));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector.from(Components(-3, -5)));
    Assert.assertEquals(Point(2.7, 4.5), positionForTimeOf(movableMock)(Duration(100, Millisecond)))
  }
}