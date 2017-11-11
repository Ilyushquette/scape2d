package scape.scape2d.engine.motion.rotational

import org.junit.Assert
import org.junit.Test

class RotationalFunctionsTest {
  @Test
  def testAsRadiansPerTimestepCounterclockwise = {
    val radiansPerSecond = 0.785398; // 45 degrees
    Assert.assertEquals(0.0785398, asRadiansPerTimestep(radiansPerSecond, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerTimestepClockwise = {
    val radiansPerSecond = -1.5708; // -90 degrees
    Assert.assertEquals(-0.15708, asRadiansPerTimestep(radiansPerSecond, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondCounterclockwise = {
    val radiansPerTimestep = 0.174533; // 10 degrees
    Assert.assertEquals(1.74533, asRadiansPerSecond(radiansPerTimestep, 100), 0.00001);
  }
  
  @Test
  def testAsRadiansPerSecondClockwise = {
    val radiansPerTimestep = -3.14159; // 180 degrees
    Assert.assertEquals(-31.4159, asRadiansPerSecond(radiansPerTimestep, 100), 0.00001);
  }
}