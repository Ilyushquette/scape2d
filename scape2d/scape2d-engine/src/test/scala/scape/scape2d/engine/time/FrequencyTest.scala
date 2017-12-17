package scape.scape2d.engine.time

import org.junit.Test
import org.junit.Assert

class FrequencyTest {
  @Test
  def testOccurenceDuration = {
    val frequency = Frequency(5, Second);
    Assert.assertEquals(0.2(Second), frequency.occurenceDuration);
  }
}