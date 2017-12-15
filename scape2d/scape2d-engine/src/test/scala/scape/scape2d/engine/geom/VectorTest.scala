package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class VectorTest {
  @Test
  def testVectorFromComponents = {
    val components = Components(3, 3);
    val vector = Vector.from(components);
    Assert.assertEquals(4.24264, vector.magnitude, 0.00001);
    Assert.assertEquals(45, vector.angle, 0.00001);
  }
  
  @Test
  def testEqualsSame = Assert.assertEquals(Vector(3, 55.5), Vector(3, 55.5));
  
  @Test
  def testEqualsSameWithZeroMagnitude = Assert.assertEquals(Vector(0, 55.5), Vector(0, 90));
  
  @Test
  def testEqualsNotSameByMagnitude = Assert.assertNotSame(Vector(78, 13.23), Vector(77, 13.23));
  
  @Test
  def testEqualsNotSameByAngle = Assert.assertNotSame(Vector(78, 13.23), Vector(78, 13.23));
  
  @Test
  def testCalculateComponents = {
    val vector = Vector(7, 15);
    val components = vector.components;
    Assert.assertEquals(6.76148, components.x, 0.00001);
    Assert.assertEquals(1.81173, components.y, 0.00001);
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = Vector(20, 0) + Vector(30, 180);
    assertVectors(Vector(10, 180), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = Vector(10, 45) + Vector(7, 45) + Vector(30, 45);
    assertVectors(Vector(47, 45), vector);
  }
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = Vector(4, 90) - Vector(5, 270);
    assertVectors(Vector(9, 90), vector);
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = Vector(3, 315) - Vector(4, 315) - Vector(5, 315);
    assertVectors(Vector(6, 135), vector);
  }
  
  @Test
  def testDotProduct = Assert.assertEquals(-9, Vector(3, 90) * Vector(4.24264, 315), 0.00001);
  
  @Test
  def testDotProductWithZeroLength = Assert.assertEquals(0, Vector(3, 90) * Vector(0, 180), 0.00001);
  
  @Test
  def testVectorMagnitudeMultiplication = {
    val sourceVector = Vector(4, 180);
    Assert.assertEquals(Vector(16, 180), sourceVector * 4);
  }
  
  @Test
  def testVectorNegativeMagnitudeMultiplication = {
    val sourceVector = Vector(3, 270);
    Assert.assertEquals(Vector(3, 90), sourceVector * -1);
  }
  
  @Test
  def testCrossProduct = {
    val v1 = Vector.from(Components(-2, 3));
    val v2 = Vector.from(Components(0, 1));
    Assert.assertEquals(-2, v1 x v2, 0.00001);
  }
  
  @Test
  def testCrossProductZeroMagnitude = {
    val v1 = Vector.from(Components(2, 3));
    val v2 = Vector(0, 90);
    Assert.assertEquals(0, v1 x v2, 0.00001);
  }
  
  @Test
  def testScalarProjection = {
    val vector1 = Vector.from(Components(0, -6));
    val vector2 = Vector.from(Components(4, -4));
    Assert.assertEquals(0.66666, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testScalarProjectionSameVectors = {
    val vector1 = Vector.from(Components(0, -6));
    val vector2 = Vector.from(Components(0, -6));
    Assert.assertEquals(1, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testScalarProjectionPerpendicularVectors = {
    val vector1 = Vector.from(Components(0, -6));
    val vector2 = Vector.from(Components(6, 0));
    Assert.assertEquals(0, vector2.scalarProjection(vector1), 0.00001);
  }
  
  @Test
  def testVectorProjection = {
    val vector1 = Vector.from(Components(-4, 0));
    val vector2 = Vector.from(Components(-7, 10));
    val projectionVector = vector2.projection(vector1);
    Assert.assertEquals(7, projectionVector.magnitude, 0.00001);
    Assert.assertEquals(180, projectionVector.angle, 0.00001);
  }
  
  @Test
  def testOpposite = {
    val sourceVector = Vector(5, 320)
    Assert.assertEquals(Vector(5, 140), sourceVector.opposite);
  }
  
  private def assertVectors(expected:Vector, actual:Vector) = {
    Assert.assertEquals(expected.magnitude, actual.magnitude, 0.00001);
    Assert.assertEquals(expected.angle, actual.angle, 0.00001);
  }
}