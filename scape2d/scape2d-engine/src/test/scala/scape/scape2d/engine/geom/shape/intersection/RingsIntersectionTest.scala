package scape.scape2d.engine.geom.shape.intersection

import scape.scape2d.engine.geom.shape.Ring
import org.junit.Test
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RingsIntersectionTest {
  @Test
  def testRingsApartOfEachOtherDontIntersect = {
    val ring1 = Ring(Circle(Point.origin, 10), 6);
    val ring2 = Ring(Circle(Point(27, 0), 10), 6);
    Assert.assertFalse(ring1 intersects ring2);
  }
  
  @Test
  def testRingInTheHoleOfAnotherDontIntersect = {
    val ring1 = Ring(Circle(Point.origin, 10), 6);
    val ring2 = Ring(Circle(Point.origin, 2.5), 1);
    Assert.assertFalse(ring1 intersects ring2);
  }
  
  @Test
  def testRingsChainLikeConnectionDoIntersect = {
    val ring1 = Ring(Circle(Point.origin, 10), 6);
    val ring2 = Ring(Circle(Point(25, 0), 10), 6);
    Assert.assertTrue(ring1 intersects ring2);
  }
  
  @Test
  def testRingInsideAnotherDoIntersect = {
    val ring1 = Ring(Circle(Point.origin, 10), 6);
    val ring2 = Ring(Circle(Point(0, 5), 6), 2);
    Assert.assertTrue(ring1 intersects ring2);
  }
}