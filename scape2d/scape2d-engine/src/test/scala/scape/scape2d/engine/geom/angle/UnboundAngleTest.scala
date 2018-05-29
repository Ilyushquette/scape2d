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
  
  def testUnitConversionUnboundAngle = {
    val convertedAngle = UnboundAngle(180, Degree) to Radian;
    Assert.assertEquals(3.14159265358979, convertedAngle.value, Epsilon);
    Assert.assertEquals(Radian, convertedAngle.unit);
  }
  
  @Test
  def testSumWithUnboundAngle = {
    Assert.assertEquals(UnboundAngle(400, Degree), UnboundAngle(540, Degree) + UnboundAngle(-140, Degree));
  }
  
  @Test
  def testSubtractionOfUnboundAngle = {
    Assert.assertEquals(UnboundAngle(-4, Radian), UnboundAngle(-1, Radian) - UnboundAngle(3, Radian));
  }
}