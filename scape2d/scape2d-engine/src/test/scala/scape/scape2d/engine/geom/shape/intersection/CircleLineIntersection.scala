package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Line

class CircleLineIntersection {
  @Test
  def testCircleLineDontIntersect = {
    val circle = Circle(Point(0, 0), 4);
    val line = Line(Point(0, 10), Point(10, 0));
    Assert.assertFalse(circle.intersects(line));
  }
  
  @Test
  def testCircleLineDoIntersect = {
    val circle = Circle(Point(0, 0), 7.5);
    val line = Line(Point(0, 10), Point(10, 0));
    Assert.assertTrue(circle.intersects(line));
  }
}