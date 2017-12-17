package scape.scape2d.engine.time

import org.junit.Test
import org.junit.Assert

class FrequencyTest {
  @Test
  def testOccurenceDuration = {
    val frequency = Frequency(5, Second);
    Assert.assertEquals(0.2(Second), frequency.occurenceDuration);
  }
  
  @Test
  def testDifferentFrequenciesWithSameTypes = {
    Assert.assertFalse(Frequency(3, Millisecond) == Frequency(2, Millisecond));
  }
  
  @Test
  def testSameFrequenciesWithSameTypes = {
    Assert.assertTrue(Frequency(5, Second) == Frequency(15, 3(Second)));
  }
  
  @Test
  def testDifferentFrequenciesWithDifferentTypes = {
    Assert.assertFalse(Frequency(1, Millisecond) == Frequency(999, Second));
  }
  
  @Test
  def testSameFrequenciesWithDifferentTypes = {
    Assert.assertTrue(Frequency(5, Second) == Frequency(300, Minute));
  }
}