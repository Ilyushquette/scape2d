package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle

class VectorTest {
  @Test
  def testVectorFromComponents = {
    val components = Components(3, 3);
    Assert.assertEquals(Vector(4.2426406871, 45(Degree)), Vector.from(components));
  }
  
  @Test
  def testEqualsSame = Assert.assertEquals(Vector(3, 55.5(Degree)), Vector(3, 55.5(Degree)));
  
  @Test
  def testEqualsSameWithZeroMagnitude = Assert.assertEquals(Vector(0, 55.5(Degree)), Vector(0, 90(Degree)));
  
  @Test
  def testEqualsNotSameByMagnitude = Assert.assertNotSame(Vector(78, 13.23(Degree)), Vector(77, 13.23(Degree)));
  
  @Test
  def testEqualsNotSameByAngle = Assert.assertNotSame(Vector(78, 13.23(Degree)), Vector(78, 13.23(Degree)));
  
  @Test
  def testCalculateComponents = {
    val vector = Vector(7, 15(Degree));
    Assert.assertEquals(Components(6.7614807840, 1.8117333157), vector.components);
  }
  
  @Test
  def testSumOpposedVectors = {
    val vector = Vector(20, 0(Degree)) + Vector(30, 180(Degree));
    Assert.assertEquals(Vector(10, 180(Degree)), vector);
  }
  
  @Test
  def testSumUnidirectionalVectors = {
    val vector = Vector(10, 45(Degree)) + Vector(7, 45(Degree)) + Vector(30, 45(Degree));
    Assert.assertEquals(Vector(47, 45(Degree)), vector);
  }
  
  @Test
  def testSubtractOpposedVectors = {
    val vector = Vector(4, 90(Degree)) - Vector(5, 270(Degree));
    Assert.assertEquals(Vector(9, 90(Degree)), vector)
  }
  
  @Test
  def testSubtractUnidirectionalVectors = {
    val vector = Vector(3, 315(Degree)) - Vector(4, 315(Degree)) - Vector(5, 315(Degree));
    Assert.assertEquals(Vector(6, 135(Degree)), vector);
  }
  
  @Test
  def testDotProduct = Assert.assertEquals(-9, Vector(3, 90(Degree)) * Vector(4.24264, 315(Degree)), 0.00001);
  
  @Test
  def testDotProductWithZeroLength = Assert.assertEquals(0, Vector(3, 90(Degree)) * Vector(0, 180(Degree)), 0.00001);
  
  @Test
  def testVectorMagnitudeMultiplication = {
    val sourceVector = Vector(4, 180(Degree));
    Assert.assertEquals(Vector(16, 180(Degree)), sourceVector * 4);
  }
  
  @Test
  def testVectorNegativeMagnitudeMultiplication = {
    val sourceVector = Vector(3, 270(Degree));
    Assert.assertEquals(Vector(3, 90(Degree)), sourceVector * -1);
  }
  
  @Test
  def testVectorMagnitudeDivision = {
    val sourceVector = Vector(5, 45(Degree));
    Assert.assertEquals(Vector(2.5, 45(Degree)), sourceVector / 2);
  }
  
  @Test
  def testVectorNegativeMagnitudeDivision = {
    val sourceVector = Vector(1, 135(Degree));
    Assert.assertEquals(Vector(1, 315(Degree)), sourceVector / -1);
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
    val v2 = Vector(0, 90(Degree));
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
    Assert.assertEquals(Vector(7, 180(Degree)), vector2 projection vector1);
  }
  
  @Test
  def testOpposite = {
    val sourceVector = Vector(5, 320(Degree))
    Assert.assertEquals(Vector(5, 140(Degree)), sourceVector.opposite);
  }
}