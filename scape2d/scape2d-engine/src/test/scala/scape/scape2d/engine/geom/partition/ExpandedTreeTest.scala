package scape.scape2d.engine.geom.partition

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.FormedMock
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class ExpandedTreeTest {
  @Test
  def testInitializationTopLeftRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-15, 5), 5, 5), expandedTree.topLeft);
  }
  
  @Test
  def testInitializationTopCenterRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-10, 5), 20, 5), expandedTree.topCenter);
  }
  
  @Test
  def testInitializationTopRightRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(10, 5), 5, 5), expandedTree.topRight);
  }
  
  @Test
  def testInitializationCenterLeftRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-15, -5), 5, 10), expandedTree.centerLeft);
  }
  
  @Test
  def testInitializationCenterRightRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(10, -5), 5, 10), expandedTree.centerRight);
  }
  
  @Test
  def testInitializationBottomLeftRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-15, -10), 5, 5), expandedTree.bottomLeft);
  }
  
  @Test
  def testInitializationBottomCenterRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-10, -10), 20, 5), expandedTree.bottomCenter);
  }
  
  @Test
  def testInitializationBottomRightRegion = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(10, -10), 5, 5), expandedTree.bottomRight);
  }
  
  @Test
  def testInitializationFinalBounds = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    Assert.assertEquals(AxisAlignedRectangle(Point(-15, -10), 30, 20), expandedTree.bounds);
  }
  
  @Test
  def testInsertionEntityCannotBeContained = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    val entity = FormedMock(Circle(Point(0, -9), 2));
    val inserted = expandedTree.insert(entity);
    Assert.assertTrue(!inserted && expandedTree.entities.isEmpty);
  }
  
  @Test
  def testInsertionEntityOnTheEdgeBetweenExpansionCells = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    val entity = FormedMock(Circle(Point(-10, -7.5), 2));
    val inserted = expandedTree.insert(entity);
    Assert.assertTrue(inserted && expandedTree.entities.contains(entity));
  }
  
  @Test
  def testInsertionEntityCanBeContained = {
    val coreBucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-10, -5), 20, 10));
    val expandedTree = new ExpandedTree(
        coreNode = coreBucket,
        expansion = 5
    );
    val entity = FormedMock(Circle(Point(0, -8), 2));
    val inserted = expandedTree.insert(entity);
    Assert.assertTrue(inserted && expandedTree.entities.isEmpty);
  }
}