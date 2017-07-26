package scape.scape2d.engine.util

import org.junit.Assert
import org.junit.Test

class Combination2Test {
  @Test
  def testDontEquals = Assert.assertNotEquals(Combination2('A', 'B'), Combination2('A', 'C'));
  
  @Test
  def testSameOrderEquals = Assert.assertEquals(Combination2(1, 2), Combination2(1, 2));
  
  @Test
  def testDifferentOrderEquals = Assert.assertEquals(Combination2("A", "B"), Combination2("B", "A"));
  
  @Test
  def testReversed = Assert.assertEquals(Combination2(321, 123), Combination2(123, 321).reversed);
}