package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point

class LinearMotionFunctionsTest {
  @Test
  def testAsMetersPerTimestep = {
    val metersPerSecond = Vector(1, 0.7853981633);
    Assert.assertEquals(Vector(0.1, 0.7853981633), asMetersPerTimestep(metersPerSecond, 100));
  }
  
  @Test
  def testAsMetersPerSecond = {
    val metersPerTimestep = Vector(0.5, 4.7123889803);
    Assert.assertEquals(Vector(5, 4.7123889803), asMetersPerSecond(metersPerTimestep, 100));
  }
  
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector(0, 0.8203047484));
    Assert.assertEquals(Point(3, 5), positionForTimeOf(movableMock)(100));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector.from(Components(-3, -5)));
    Assert.assertEquals(Point(2.7, 4.5), positionForTimeOf(movableMock)(100))
  }
}