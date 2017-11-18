package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Components2D
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Point

class LinearMotionFunctionsTest {
  @Test
  def testAsMetersPerTimestep = {
    val metersPerSecond = new Vector2D(1, 45);
    Assert.assertEquals(new Vector2D(0.1, 45), asMetersPerTimestep(metersPerSecond, 100));
  }
  
  @Test
  def testAsMetersPerSecond = {
    val metersPerTimestep = new Vector2D(0.5, 270);
    Assert.assertEquals(new Vector2D(5, 270), asMetersPerSecond(metersPerTimestep, 100));
  }
  
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(new Vector2D(0, 47));
    Assert.assertEquals(Point(3, 5), getPostLinearMotionPosition(movableMock, 100));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = Mockito.mock(classOf[Movable]);
    Mockito.when(movableMock.position).thenReturn(Point(3, 5));
    Mockito.when(movableMock.velocity).thenReturn(Vector2D.from(Components2D(-3, -5)));
    Assert.assertEquals(Point(2.7, 4.5), getPostLinearMotionPosition(movableMock, 100))
  }
}