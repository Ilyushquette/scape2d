package scape.scape2d.engine.geom.angle

import org.junit.Assert
import org.junit.Test

class AngleTest {
  @Test
  def testDifferentAnglesWithSameTypes = Assert.assertFalse(90(Degree) == 270(Degree));
  
  @Test
  def testSameAnglesWithSameTypes = Assert.assertTrue(45(Degree) == 45(Degree));
  
  @Test
  def testDifferentDurationWithDifferentTypes = Assert.assertFalse(30(Degree) == Angle.straight);
  
  @Test
  def testSameDurationWithDifferentTypes = Assert.assertTrue(90(Degree) == 1.5707963267(Radian));
  
  @Test
  def testAngleMultiplicationByNumber = Assert.assertEquals(135(Degree), 90(Degree) * 5.5);
  
  @Test
  def testAngleDivisionByAnotherAngle = Assert.assertEquals(9, 45(Degree) / 5(Degree), Epsilon);
  
  @Test
  def testAngleDivisionByNumber = Assert.assertEquals(3(Degree), 270(Degree) / 90);
}