package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class Vector2DTest {
  @Test
  def testVectorFromComponents = {
    val components = Components2D(6.761480784023478, 1.8117333157176452);
    Assert.assertEquals(new Vector2D(7, 15), Vector2D.from(components));
  }
  
  @Test
  def testEqualsSame = Assert.assertEquals(new Vector2D(3, 55.5), new Vector2D(3, 55.5));
  
  @Test
  def testEqualsNotSame = Assert.assertNotSame(new Vector2D(78, 13.23), new Vector2D(77, 13.23));
  
  @Test
  def testCalculateComponents = {
    val vector = new Vector2D(7, 15);
    Assert.assertEquals(Components2D(6.761480784023478, 1.8117333157176452), vector.components);
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = new Vector2D(20, 0) + new Vector2D(30, 180);
    Assert.assertEquals(new Vector2D(10, 180), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = new Vector2D(10, 45) + new Vector2D(7, 45) + new Vector2D(30, 45);
    Assert.assertEquals(new Vector2D(47, 45), vector);
  }
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = new Vector2D(4, 90) - new Vector2D(5, 270);
    Assert.assertEquals(new Vector2D(9, 90), vector);
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = new Vector2D(3, 315) - new Vector2D(4, 315) - new Vector2D(5, 315);
    Assert.assertEquals(new Vector2D(6.000000000000001, 135), vector);
  }
}