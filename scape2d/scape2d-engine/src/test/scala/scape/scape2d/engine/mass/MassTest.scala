package scape.scape2d.engine.mass

import org.junit.Assert
import org.junit.Test

class MassTest {
  @Test
  def testDifferentMassesWithSameTypes = Assert.assertFalse(1(Ton) == 0.999(Ton));
  
  @Test
  def testSameMassesWithSameTypes = Assert.assertTrue(200(Gram) == 200(Gram));
  
  @Test
  def testDifferentMassesWithDifferentTypes = Assert.assertFalse(1500(Gram) == 2(Kilogram));
  
  @Test
  def testSameMassesWithDifferentTypes = Assert.assertTrue(500(Kilogram) == 0.5(Ton));
  
  @Test
  def testUnitConversionMass = {
    val convertedMass = 64(Kilogram) to Gram;
    Assert.assertEquals(64000, convertedMass.value, Epsilon);
    Assert.assertEquals(Gram, convertedMass.unit);
  }
  
  @Test
  def testMassSumWithAnotherMass = Assert.assertEquals(1(Ton), 700(Kilogram) + 300000(Gram));
  
  @Test
  def testMassSubtractionFromAnotherMass = Assert.assertEquals(400(Gram), Kilogram - 0.6(Kilogram));
  
  @Test
  def testMassMultiplicationByNumber = Assert.assertEquals(1(Kilogram), 250(Gram) * 4);
  
  @Test
  def testMassDivisionByAnotherMass = Assert.assertEquals(10, Ton / 100(Kilogram), Epsilon);
  
  @Test
  def testMassDivisionByNumber = Assert.assertEquals(1(Milligram), Gram / 1000);
}