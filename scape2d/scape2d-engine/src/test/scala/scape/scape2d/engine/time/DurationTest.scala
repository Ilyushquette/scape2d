package scape.scape2d.engine.time

import org.junit.Test
import org.junit.Assert

class DurationTest {
  @Test
  def testDifferentDurationsWithSameTypes = Assert.assertFalse(0.5(Second) == Second);
  
  @Test
  def testSameDurationsWithSameTypes = Assert.assertTrue(5(Millisecond) == 5(Millisecond));
  
  @Test
  def testDifferentDurationWithDifferentTypes = Assert.assertFalse(59.9(Second) == Minute);
  
  @Test
  def testSameDurationWithDifferentTypes = Assert.assertTrue(10(Second) == 10000(Millisecond));
  
  @Test
  def testUnitConversionDuration = Assert.assertEquals(3000000(Nanosecond), 3(Millisecond) to Nanosecond);
  
  @Test
  def testDurationSumWithAnotherDuration = Assert.assertEquals(1.5(Minute), 30(Second) + Minute);
  
  @Test
  def testDurationSubtrationFromAnotherDuration = Assert.assertEquals(0.5(Second), Second - 500(Millisecond));
  
  @Test
  def testDurationMultiplicationByNumber = Assert.assertEquals(3(Hour), 1.5(Hour) * 2);
  
  @Test
  def testDurationDivisionByAnotherDuration = Assert.assertEquals(24, 2(Minute) / 5(Second), 0.00001);
  
  @Test
  def testDurationDivisionByNumber = Assert.assertEquals(30(Second), Minute / 2);
}