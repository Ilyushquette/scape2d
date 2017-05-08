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
}