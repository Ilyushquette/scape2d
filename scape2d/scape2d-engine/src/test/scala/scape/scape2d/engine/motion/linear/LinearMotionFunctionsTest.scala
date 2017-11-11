package scape.scape2d.engine.motion.linear

import org.junit.Test
import scape.scape2d.engine.geom.Vector2D
import org.junit.Assert
import org.junit.Test

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
}