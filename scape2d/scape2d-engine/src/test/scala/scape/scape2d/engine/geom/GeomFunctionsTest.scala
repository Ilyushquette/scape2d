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
    Assert.assertEquals(new Vector2D(7, 15), calculateVector(components));
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = sum(List(new Vector2D(20, 0), new Vector2D(30, 180)));
    Assert.assertEquals(new Vector2D(10, 180), vector);
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
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = subtract(List(new Vector2D(4, 90), new Vector2D(5, 270)));
    Assert.assertEquals(new Vector2D(9, 90), vector);
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = subtract(List(new Vector2D(3, 315), new Vector2D(4, 315), new Vector2D(5, 315)));
    Assert.assertEquals(new Vector2D(6, 135), vector);
  }
  
  @Test
  def testSubtractSingletonEqualsVectorItself = {
    val vector = subtract(List(new Vector2D(5, 200)));
    Assert.assertEquals(new Vector2D(5, 199.99999999999997), vector);
  }
}