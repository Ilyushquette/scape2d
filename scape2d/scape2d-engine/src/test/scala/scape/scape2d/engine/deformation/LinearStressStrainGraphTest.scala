package scape.scape2d.engine.deformation

import org.junit.Test
import org.junit.Assert

class LinearStressStrainGraphTest {
  @Test
  def testStrainResolution = {
    val linearGraph = LinearStressStrainGraph(5);
    Assert.assertEquals(0.5, linearGraph.forStress(2.5), 0.00001);
  }
  
  @Test
  def testStressResolution = {
    val linearGraph = LinearStressStrainGraph(5);
    Assert.assertEquals(-20, linearGraph.forStrain(-4), 0.00001);
  }
}