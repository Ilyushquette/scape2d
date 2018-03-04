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
  
  @Test
  def testSelectFromEmptyCollectionEmptyCombinations = {
    val numbers = Set[Int]();
    Assert.assertEquals(Set(), Combination2.selectFrom(numbers));
  }
  
  @Test
  def testSelectFromSingletonCollectionEmptyCombinations = {
    val numbers = Set(3);
    Assert.assertEquals(Set(), Combination2.selectFrom(numbers));
  }
  
  @Test
  def testSelectFromCollectionDistinctCombinations = {
    val numbers = Set(1, 3, 7, 4);
    val expectedCombinations = Set(Combination2(1, 3),
                                   Combination2(1, 7),
                                   Combination2(1, 4),
                                   Combination2(3, 7),
                                   Combination2(3, 4),
                                   Combination2(7, 4));
    Assert.assertEquals(expectedCombinations, Combination2.selectFrom(numbers));
  }
}