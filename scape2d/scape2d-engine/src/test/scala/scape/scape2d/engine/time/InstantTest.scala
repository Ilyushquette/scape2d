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
}