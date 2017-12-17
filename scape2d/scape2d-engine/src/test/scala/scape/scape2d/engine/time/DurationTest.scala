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
  
  @Test
  def testDurationLessThanAnotherDuration = Assert.assertTrue(59(Second) < Minute);
  
  @Test
  def testDurationNotLessThanAnotherDuration = Assert.assertFalse(61(Second) < Minute);
  
  @Test
  def testDurationLessThanOrEqualToAnotherDurationLess = Assert.assertTrue(999(Millisecond) <= Second);
  
  @Test
  def testDurationLessThanOrEqualToAnotherDurationEqual = Assert.assertTrue(60(Millisecond) <= Minute);
  
  @Test
  def testDurationNotLessThanOrEqualToAnotherDuration = Assert.assertFalse(10000001(Nanosecond) <= Millisecond);
  
  @Test
  def testDurationGreaterThanAnotherDuration = Assert.assertTrue(10000001(Nanosecond) > Millisecond);
  
  @Test
  def testDurationNotGreaterThanAnotherDuration = Assert.assertFalse(Second > 1000(Millisecond));
  
  @Test
  def testDurationGreaterThanOrEqualToAnotherDurationGreater = Assert.assertTrue(Day > 23(Hour));
  
  @Test
  def testDurationGreaterThanOrEqualToAnotherDurationEqual = Assert.assertTrue(Hour >= 60(Minute));
  
  @Test
  def testDurationNotGreaterThanOrEqualToAnotherDuration = Assert.assertFalse(14(Minute) >= 0.25(Hour));
}