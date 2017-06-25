package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point

class PointsIntersectionTest {
  @Test
  def testPointsSameCoordsDoIntersect = {
    Assert.assertTrue(Point(50.7, 25.3).intersects(Point(50.7, 25.3)));
  }
  
  @Test
  def testPointsDifferentCoordsDontIntersect = {
    Assert.assertFalse(Point(-50.7, 25.3).intersects(Point(-50.7, 25)));
  }
}