package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Ring

class SecondMomentOfAreaTest {
  @Test
  def testCircleSecondMomentOfArea = {
    val circle = Circle(Point(7, 10), 10);
    Assert.assertEquals(SecondMomentOfArea(15707.963267948966), SecondMomentOfArea(circle));
  }
  
  @Test
  def testRectangleSecondMomentOfArea = {
    val rectangle = AxisAlignedRectangle(Point.origin, 4, 5);
    Assert.assertEquals(SecondMomentOfArea(68.33333333333333), SecondMomentOfArea(rectangle));
  }
  
  @Test
  def testTriangularPolygonSecondMomentOfArea = {
    val polygon = PolygonBuilder(Point.origin, Point(0, 10), Point(5, 0)).build;
    Assert.assertEquals(SecondMomentOfArea(173.61111111111114), SecondMomentOfArea(polygon));
  }
  
  @Test
  def testCircleSweepSecondMomentOfArea = {
    val circleSweep = CircleSweep(Circle(Point(-5, 5), 3), Vector(10, Angle.bound(315, Degree)));
    Assert.assertEquals(SecondMomentOfArea(1874.0999706681694), SecondMomentOfArea(circleSweep));
  }
  
  @Test
  def testRingSecondMomentOfArea = {
    val ring = Ring(Circle(Point(-10, -10), 5), 2);
    Assert.assertEquals(SecondMomentOfArea(1633.6281798666923), SecondMomentOfArea(ring));
  }
}