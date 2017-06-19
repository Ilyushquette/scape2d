package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point

class TripletOrientationTest {
  @Test
  def testClockwiseOrientationAcuteAngle = {
    Assert.assertEquals(Clockwise, TripletOrientation(Point(-1, 1), Point(2, 1), Point(0, 0)));
  }
  
  @Test
  def testClockwiseOrientationObtuseAngle = {
    Assert.assertEquals(Clockwise, TripletOrientation(Point(3, 5), Point(5, 5), Point(7, 2)));
  }
  
  @Test
  def testCounterClockwiseOrientationAcuteAngle = {
    Assert.assertEquals(CounterClockwise, TripletOrientation(Point(0, 0), Point(2, 1), Point(-1, 1)));
  }
  
  @Test
  def testCounterClockwiseOrientationObtuseAngle = {
    Assert.assertEquals(CounterClockwise, TripletOrientation(Point(7, 2), Point(5, 5), Point(3, 5)));
  }
  
  @Test
  def testCollinearOrientationStraightAngle = {
    Assert.assertEquals(Collinear, TripletOrientation(Point(3, 3), Point(4, 4), Point(6.5, 6.5)));
  }
  
  @Test
  def testCollinearOrientationReversedAngle = {
    Assert.assertEquals(Collinear, TripletOrientation(Point(3, 3), Point(4, 4), Point(3.5, 3.5)));
  }
}