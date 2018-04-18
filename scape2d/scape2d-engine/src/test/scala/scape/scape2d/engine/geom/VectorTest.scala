package scape.scape2d.engine.geom

import java.lang.Math.PI
import org.junit.Assert
import org.junit.Test

class VectorTest {
  @Test
  def testVectorFromComponents = {
    val components = Components(3, 3);
    val vector = Vector.from(components);
    Assert.assertEquals(Vector(4.2426406871, 0.7853981633), vector);
  }
  
  @Test
  def testEqualsSame = Assert.assertEquals(Vector(3, 0.96865), Vector(3, 0.96865));
  
  @Test
  def testEqualsSameWithZeroMagnitude = Assert.assertEquals(Vector(0, 0.96865), Vector(0, HalfPI));
  
  @Test
  def testEqualsNotSameByMagnitude = Assert.assertNotEquals(Vector(78, 0.23090), Vector(77, 0.23090));
  
  @Test
  def testEqualsNotSameByAngle = Assert.assertNotEquals(Vector(78, 0.23090), Vector(78, -0.23090));
  
  @Test
  def testCalculateComponents = {
    val vector = Vector(7, 0.2617993877);
    val components = vector.components;
    Assert.assertEquals(6.76148, components.x, 0.00001);
    Assert.assertEquals(1.81173, components.y, 0.00001);
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = Vector(20, 0) + Vector(30, PI);
    assertVectors(Vector(10, PI), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = Vector(10, 0.7853981633) + Vector(7, 0.7853981633) + Vector(30, 0.7853981633);
    assertVectors(Vector(47, 0.7853981633), vector);
  }
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = Vector(4, HalfPI) - Vector(5, 4.7123889803);
    assertVectors(Vector(9, HalfPI), vector);
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = Vector(3, 5.4977871437) - Vector(4, 5.4977871437) - Vector(5, 5.4977871437);
    assertVectors(Vector(6, 2.3561944901), vector);
  }
  
  @Test
  def testDotProduct = Assert.assertEquals(-9, Vector(3, HalfPI) * Vector(4.24264, 5.4977871437), 0.00001);
  
  @Test
  def testDotProductWithZeroLength = Assert.assertEquals(0, Vector(3, HalfPI) * Vector(0, PI), 0.00001);
  
  @Test
  def testVectorMagnitudeMultiplication = {
    val sourceVector = Vector(4, PI);
    Assert.assertEquals(Vector(16, PI), sourceVector * 4);
  }
  
  @Test
  def testVectorNegativeMagnitudeMultiplication = {
    val sourceVector = Vector(3, 4.7123889803);
    Assert.assertEquals(Vector(3, HalfPI), sourceVector * -1);
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
    val v2 = Vector(0, HalfPI);
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
    Assert.assertEquals(Vector(7, PI), projectionVector);
  }
  
  @Test
  def testOpposite = {
    val sourceVector = Vector(5, 5.5850536063);
    Assert.assertEquals(Vector(5, 2.4434609527), sourceVector.opposite);
  }
  
  private def assertVectors(expected:Vector, actual:Vector) = {
    Assert.assertEquals(expected.magnitude, actual.magnitude, 0.00001);
    Assert.assertEquals(expected.angle, actual.angle, 0.00001);
  }
}