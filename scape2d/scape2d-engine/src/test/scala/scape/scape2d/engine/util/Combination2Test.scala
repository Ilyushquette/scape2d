package scape.scape2d.engine.util

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.FormedMock
import scape.scape2d.engine.geom.partition.NodeMock
import scape.scape2d.engine.geom.shape.Point

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
  
  @Test 
  def testSelectFromTree = {
    val entity1 = new FormedMock(Point(1, 1));
    val entity2 = new FormedMock(Point(-1, 1));
    val entity3 = new FormedMock(Point(1, -1));
    val entity4 = new FormedMock(Point(-1, -1));
    val entity5 = new FormedMock(Point.origin);
    val entity6 = new FormedMock(Point(2, 0));
    val node = new NodeMock(_entities = List(entity1, entity2));
    val node2 = new NodeMock(_entities = List(entity3, entity4, entity5));
    val node3 = new NodeMock(_entities = List(entity6));
    node._nodes = node.nodes :+ node2 :+ node3;
    val expectedCombinationsBranch1 = Combination2.selectFrom(List(entity1, entity2, entity3, entity4, entity5));
    val expectedCombinationsBranch2 = Combination2.selectFrom(List(entity1, entity2, entity6));
    Assert.assertEquals(expectedCombinationsBranch1 ++ expectedCombinationsBranch2, Combination2.selectFrom(node));
  }
}