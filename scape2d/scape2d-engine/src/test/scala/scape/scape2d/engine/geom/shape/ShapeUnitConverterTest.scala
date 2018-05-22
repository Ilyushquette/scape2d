package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle

class ShapeUnitConverterTest {
  @Test
  def testPointUnitConversion = {
    val point = Point(3, -2.5);
    val converter = new ShapeUnitConverter(33);
    Assert.assertEquals(Point(99, -82.5), converter.scale(point));
  }
  
  @Test
  def testSegmentUnitConversion = {
    val segment = Segment(Point(1, 0.5), Point(3, -0.5));
    val converter = new ShapeUnitConverter(50);
    Assert.assertEquals(Segment(Point(50, 25), Point(150, -25)), converter.scale(segment));
  }
  
  @Test
  def testCircleUnitConversion = {
    val circle = Circle(Point(0.25, 0.25), 0.05);
    val converter = new ShapeUnitConverter(33);
    Assert.assertEquals(Circle(Point(8.25, 8.25), 1.65), converter.scale(circle));
  }
  
  @Test
  def testAxisAlignedRectangleUnitConversion = {
    val aabb = AxisAlignedRectangle(Point(-3, -3), 6, 6);
    val converter = new ShapeUnitConverter(33);
    Assert.assertEquals(AxisAlignedRectangle(Point(-99, -99), 198, 198), converter.scale(aabb));
  }
  
  /**      ______
   *      /      \
   *     /        \
   *    /          \
   *   |            |
   *   |            |
   *   |            |
   *    \          / 
   *     \        /        
   *      \______/
   */
  @Test
  def testPolygonUnitConversion = {
    val polygon = PolygonBuilder(Point(0, 3), Point(3, 0), Point(6, 0))
                  .to(Point(9, 3)).to(Point(9, 6)).to(Point(6, 9)).to(Point(3, 9)).to(Point(0, 6))
                  .build;
    val converter = new ShapeUnitConverter(33);
    val expected = PolygonBuilder(Point(0, 99), Point(99, 0), Point(198, 0))
                  .to(Point(297, 99)).to(Point(297, 198)).to(Point(198, 297)).to(Point(99, 297)).to(Point(0, 198))
                  .build;
    Assert.assertEquals(expected, converter.scale(polygon));
  }
  
  @Test
  def testCircleSweepUnitConversion = {
    val circleSweep = CircleSweep(Circle(Point(3, 3), 1), Vector(2, 45(Degree)));
    val converter = new ShapeUnitConverter(33);
    Assert.assertEquals(CircleSweep(Circle(Point(99, 99), 33), Vector(66, 45(Degree))), converter.scale(circleSweep));
  }
  
  @Test
  def testRingUnitConversion = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val converter = new ShapeUnitConverter(33);
    Assert.assertEquals(Ring(Circle(Point(0, 0), 330), 198), converter.scale(ring));
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testLineUnitsIllegalConversion:Unit = {
    val line = Line(Point(0, 0), Point(0, 1));
    val converter = new ShapeUnitConverter(33);
    converter.scale(line);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testRayUnitsIllegalConversion:Unit = {
    val line = Ray(Point(0, 0), 315(Degree));
    val converter = new ShapeUnitConverter(33);
    converter.scale(line);
  }
}