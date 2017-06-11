package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class Vector2DTest {
  @Test
  def testVectorFromComponents = {
    val components = Components2D(3, 3);
    val vector = Vector2D.from(components);
    Assert.assertEquals(4.24264, vector.magnitude, 0.00001);
    Assert.assertEquals(45, vector.angle, 0.00001);
  }
  
  @Test
  def testEqualsSame = Assert.assertEquals(new Vector2D(3, 55.5), new Vector2D(3, 55.5));
  
  @Test
  def testEqualsNotSame = Assert.assertNotSame(new Vector2D(78, 13.23), new Vector2D(77, 13.23));
  
  @Test
  def testCalculateComponents = {
    val vector = new Vector2D(7, 15);
    val components = vector.components;
    Assert.assertEquals(6.76148, components.x, 0.00001);
    Assert.assertEquals(1.81173, components.y, 0.00001);
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = new Vector2D(20, 0) + new Vector2D(30, 180);
    assertVectors(new Vector2D(10, 180), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = new Vector2D(10, 45) + new Vector2D(7, 45) + new Vector2D(30, 45);
    assertVectors(new Vector2D(47, 45), vector);
  }
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = new Vector2D(4, 90) - new Vector2D(5, 270);
    assertVectors(new Vector2D(9, 90), vector);
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = new Vector2D(3, 315) - new Vector2D(4, 315) - new Vector2D(5, 315);
    assertVectors(new Vector2D(6, 135), vector);
  }
  
  @Test
  def testDotProduct = Assert.assertEquals(-9, new Vector2D(3, 90) * new Vector2D(4.24264, 315), 0.00001);
  
  @Test
  def testDotProductWithZeroLength = Assert.assertEquals(0, new Vector2D(3, 90) * new Vector2D(0, 180), 0.00001);
  
  @Test
  def testVectorMagnitudeMultiplication = {
    val sourceVector = new Vector2D(4, 180);
    Assert.assertEquals(new Vector2D(16, 180), sourceVector * 4);
  }
  
  @Test
  def testVectorNegativeMagnitudeMultiplication = {
    val sourceVector = new Vector2D(3, 270);
    Assert.assertEquals(new Vector2D(3, 90), sourceVector * -1);
  }
  
  @Test
  def testScalarProjection = {
    val vector1 = Vector2D.from(Components2D(0, -6));
    val vector2 = Vector2D.from(Components2D(4, -4));
    Assert.assertEquals(0.66666, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testScalarProjectionSameVectors = {
    val vector1 = Vector2D.from(Components2D(0, -6));
    val vector2 = Vector2D.from(Components2D(0, -6));
    Assert.assertEquals(1, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testScalarProjectionPerpendicularVectors = {
    val vector1 = Vector2D.from(Components2D(0, -6));
    val vector2 = Vector2D.from(Components2D(6, 0));
    Assert.assertEquals(0, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testVectorProjection = {
    val vector1 = Vector2D.from(Components2D(-4, 0));
    val vector2 = Vector2D.from(Components2D(-7, 10));
    val projectionVector = vector2.projection(vector1);
    Assert.assertEquals(7, projectionVector.magnitude, 0.00001);
    Assert.assertEquals(180, projectionVector.angle, 0.00001);
  }
  
  @Test
  def testOpposite = {
    val sourceVector = new Vector2D(5, 320)
    Assert.assertEquals(new Vector2D(5, 140), sourceVector.opposite);
  }
  
  private def assertVectors(expected:Vector2D, actual:Vector2D) = {
    Assert.assertEquals(expected.magnitude, actual.magnitude, 0.00001);
    Assert.assertEquals(expected.angle, actual.angle, 0.00001);
  }
}