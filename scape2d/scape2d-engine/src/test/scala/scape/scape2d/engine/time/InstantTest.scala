package scape.scape2d.engine.time

import org.junit.Test
import org.junit.Assert

class InstantTest {
  @Test
  def testDifferentInstantsBeforeAndAfterUnixEpochOrigin = {
    val instantBeforeUnixEpoch = Instant(-25(Day));
    val instantAfterUnixEpoch = Instant(25(Day));
    Assert.assertFalse(instantBeforeUnixEpoch == instantAfterUnixEpoch);
  }
  
  @Test
  def testSameInstants = {
    Assert.assertTrue(Instant(30(Second)) == Instant(0.5(Minute)));
  }
  
  @Test
  def testAdditionOfDuration = {
    Assert.assertEquals(Instant(1000(Day)), Instant(999(Day)) + 24(Hour));
  }
  
  @Test
  def testSubtractionOfDuration = {
    Assert.assertEquals(Instant(-1(Day)), Instant(999(Day)) - 1000(Day));
  }
  
  @Test
  def testSubtractionOfAnotherInstant = {
    Assert.assertEquals(1(Day), Instant(1000(Day)) - Instant(999(Day)));
  }
}