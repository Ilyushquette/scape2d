package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Components2D
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point

class LinearMotionFunctionsTest {
  @Test
  def testAsMetersPerTimestep = {
    val metersPerSecond = Vector(1, 45);
    Assert.assertEquals(Vector(0.1, 45), asMetersPerTimestep(metersPerSecond, 100));
  }
  
  @Test
  def testAsMetersPerSecond = {
    val metersPerTimestep = Vector(0.5, 270);
    Assert.assertEquals(Vector(5, 270), asMetersPerSecond(metersPerTimestep, 100));
  }
  
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector(0, 47));
    Assert.assertEquals(Point(3, 5), getPostLinearMotionPosition(movableMock, 100));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector.from(Components2D(-3, -5)));
    Assert.assertEquals(Point(2.7, 4.5), getPostLinearMotionPosition(movableMock, 100))
  }
}