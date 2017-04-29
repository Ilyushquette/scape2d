package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class TestGeomFunctions {
  @Test
  def testCalculateComponents = {
    val vector = new Vector2D(7, 15);
    Assert.assertEquals(Components2D(6.761480784023478, 1.8117333157176452), calculateComponents(vector));
  }
  
  @Test
  def testCalculateVector = {
    val components = Components2D(6.761480784023478, 1.8117333157176452);
    Assert.assertEquals(new Vector2D(7, 14.999999999999998), calculateVector(components));
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = sum(List(new Vector2D(20, 0), new Vector2D(30, 180)));
    Assert.assertEquals(new Vector2D(10, 179.99999999999997), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = sum(List(new Vector2D(10, 45), new Vector2D(7, 45), new Vector2D(30, 45)));
    Assert.assertEquals(new Vector2D(47, 45), vector);
  }
  
  @Test
  def testSumSingletonEqualsVectorItself = {
    val vector = new Vector2D(3, 90);
    Assert.assertEquals(vector, sum(List(vector)));
  }
}