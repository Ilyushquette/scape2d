package scape.scape2d.engine.geom.partition

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.FormedMock
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class BucketTest {
  @Test
  def testInsertionEntityCannotBeContained = {
    val entity = FormedMock(Circle(Point(4, -10), 3));
    val bucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-5, -10), 10, 20));
    val inserted = bucket.insert(entity);
    Assert.assertTrue(!inserted && bucket.entities.isEmpty);
  }
  
  @Test
  def testInsertionEntityCanBeContained = {
    val entity = FormedMock(Circle(Point.origin, 3));
    val bucket = new Bucket[FormedMock[Circle]](AxisAlignedRectangle(Point(-5, -10), 10, 20));
    val inserted = bucket.insert(entity);
    Assert.assertTrue(inserted && bucket.entities.contains(entity));
  }
}