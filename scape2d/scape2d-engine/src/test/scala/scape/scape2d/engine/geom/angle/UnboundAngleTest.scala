package scape.scape2d.engine.geom.angle

import org.junit.Test
import org.junit.Assert

class UnboundAngleTest {
  @Test
  def testDifferentUnboundAnglesWithSameTypes = {
    Assert.assertFalse(UnboundAngle(90, Degree) == UnboundAngle(450, Degree));
  }
  
  @Test
  def testSameUnboundAnglesWithSameTypes = {
    Assert.assertTrue(UnboundAngle(35, Degree) == UnboundAngle(35, Degree));
  }
  
  @Test
  def testDifferentUnboundAnglesWithDifferentTypes = {
    Assert.assertFalse(UnboundAngle(45, Degree) == Angle.right.unbound);
  }
  
  @Test
  def testSameUnboundAnglesWithDifferentTypes = {
    Assert.assertTrue(UnboundAngle(180, Degree) == Angle.straight.unbound);
  }
}