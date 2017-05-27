package scape.scape2d.engine.util

import org.junit.Assert
import org.junit.Test

class LazyValTest {
  @Test
  def testLazyEvaluation = {
    val lazyVal = LazyVal(System.nanoTime());
    Assert.assertTrue(System.nanoTime() < lazyVal.value);
  }
}