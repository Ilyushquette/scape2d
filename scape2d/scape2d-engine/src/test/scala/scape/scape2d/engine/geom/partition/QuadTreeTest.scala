package scape.scape2d.engine.geom.partition

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.FormedMock
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class QuadTreeTest {
  @Test
  def testInsertionEntityCannotBeContained = {
    val entity = FormedMock(Circle(Point(1, 9), 3));
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val inserted = quadTree.insert(entity);
    Assert.assertTrue(!inserted && quadTree.entities.isEmpty);
  }
  
  @Test
  def testInsertionEntityCanBeContained = {
    val entity = FormedMock(Circle(Point(3, 7), 3));
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val inserted = quadTree.insert(entity);
    Assert.assertTrue(inserted && quadTree.entities.contains(entity));
  }
  
  @Test
  def testInsertionNoSplitNotOverflown = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity1 = FormedMock(Circle(Point(2, 8), 2));
    val entity2 = FormedMock(Circle(Point(8, 8), 2));
    val entity3 = FormedMock(Circle(Point(2, 2), 2));
    Assert.assertTrue(quadTree.nodes.isEmpty);
  }
  
  @Test
  def testInsertionSplitIntoSubnodesAfterOverflown = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity1 = FormedMock(Circle(Point(2, 8), 2));
    val entity2 = FormedMock(Circle(Point(8, 8), 2));
    val entity3 = FormedMock(Circle(Point(2, 2), 2));
    val entity4 = FormedMock(Circle(Point(8, 2), 2));
    quadTree.insert(entity1);
    quadTree.insert(entity2);
    quadTree.insert(entity3);
    quadTree.insert(entity4);
    val nodes = quadTree.nodes.sortBy(node => node.bounds.bottomLeft.y)(Ordering[Double].reverse);
    Assert.assertEquals(List(entity1), nodes(0).entities);
    Assert.assertEquals(List(entity2), nodes(1).entities);
    Assert.assertEquals(List(entity3), nodes(2).entities);
    Assert.assertEquals(List(entity4), nodes(3).entities);
  }
  
  @Test
  def testInsertionSplitIntoSubnodesAfterOverflownUncontainedLeftInTheNode = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity1 = FormedMock(Circle(Point(3, 7), 3));
    val entity2 = FormedMock(Circle(Point(8, 8), 2));
    val entity3 = FormedMock(Circle(Point(2, 2), 2));
    val entity4 = FormedMock(Circle(Point(7, 3), 3));
    quadTree.insert(entity1);
    quadTree.insert(entity2);
    quadTree.insert(entity3);
    quadTree.insert(entity4);
    val nodes = quadTree.nodes.sortBy(node => node.bounds.bottomLeft.y)(Ordering[Double].reverse);
    Assert.assertEquals(List(entity2), nodes(1).entities);
    Assert.assertEquals(List(entity3), nodes(2).entities);
    Assert.assertEquals(List(entity1, entity4), quadTree.entities);
  }
  
  @Test
  def testInsertionSplitIntoSubnodesWithSpecifiedParent = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entities = List(FormedMock(Circle(Point(3, 7), 3)),
                        FormedMock(Circle(Point(8, 8), 2)),
                        FormedMock(Circle(Point(2, 2), 2)),
                        FormedMock(Circle(Point(7, 3), 3)));
    entities.foreach(quadTree.insert);
    Assert.assertTrue(quadTree.nodes.forall(_.parent.get == quadTree));
  }
  
  @Test
  def testSubentitiesRecursiveNature = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entities = List(FormedMock(Circle(Point(3, 7), 3)),
                        FormedMock(Circle(Point(3, 7), 3)),
                        FormedMock(Circle(Point(2, 2), 2)),
                        FormedMock(Circle(Point(7, 3), 3)));
    entities.foreach(quadTree.insert);
    Assert.assertEquals(List(entities(2)), quadTree.subEntities);
  }
  
  @Test
  def testSuperentitiesRecursiveNature = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entities = List(FormedMock(Circle(Point(3, 7), 3)),
                        FormedMock(Circle(Point(3, 7), 3)),
                        FormedMock(Circle(Point(2, 2), 2)),
                        FormedMock(Circle(Point(7, 3), 3)));
    entities.foreach(quadTree.insert);
    Assert.assertEquals(List(entities(0), entities(1), entities(3)), quadTree.nodes(0).superEntities);
  }
  
  @Test
  def testFindTreeNodeEmptyQuadTree = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity = FormedMock(Circle(Point(3, 7), 3));
    Assert.assertEquals(None, quadTree.findTreeNode(entity));
  }
  
  @Test
  def testFindTreeNodeSingletonQuadTree = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity = FormedMock(Circle(Point(3, 7), 3));
    quadTree.insert(entity);
    Assert.assertEquals(Some(quadTree), quadTree.findTreeNode(entity));
  }
  
  @Test
  def testFindTreeNodeSplittedQuadTree = {
    val quadTree = new QuadTree[FormedMock[Circle]](AxisAlignedRectangle(Point.origin, 10, 10), 3);
    val entity1 = FormedMock(Circle(Point(2, 8), 2));
    val entity2 = FormedMock(Circle(Point(8, 8), 2));
    val entity3 = FormedMock(Circle(Point(2, 2), 2));
    val entity4 = FormedMock(Circle(Point(8, 2), 2));
    quadTree.insert(entity1);
    quadTree.insert(entity2);
    quadTree.insert(entity3);
    quadTree.insert(entity4);
    val nodes = quadTree.nodes.sortBy(node => node.bounds.bottomLeft.y)(Ordering[Double].reverse);
    Assert.assertEquals(Some(nodes(1)), quadTree.findTreeNode(entity2));
  }
}