package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.time.doubleToTime
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Minute
import scape.scape2d.engine.time.Second

class AccelerationTest {
  @Test
  def testTimeForScalarVelocityChangeResolution = {
    val acceleration = Vector(5, Angle.right) / Second / Second;
    Assert.assertEquals(500(Millisecond), acceleration.forScalarVelocityChange(150, Minute));
  }
  
  @Test
  def testVelocityChangeForTimeResolution = {
    val acceleration = Vector(3, Angle.zero) / Second / Second;
    Assert.assertEquals(Vector(9, Angle.zero) / Second, acceleration forTime 3(Second));
  }
}