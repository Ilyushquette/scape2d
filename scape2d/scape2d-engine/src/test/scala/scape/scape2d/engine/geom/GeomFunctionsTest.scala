package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class TestGeomFunctions {
  @Test
  def testNormalizePositiveAngle = {
    Assert.assertEquals(135, normalizeAngle(135), 0.00001);
  }
  
  @Test
  def testNormalizeNegativeAngle = {
    Assert.assertEquals(270, normalizeAngle(-90), 0.00001);
  }
  
  @Test
  def testCosDeg = Assert.assertEquals(0.5446, cosDeg(57), 0.0001);
  
  @Test
  def testSinDeg = Assert.assertEquals(0.2588, sinDeg(15), 0.0001);
}