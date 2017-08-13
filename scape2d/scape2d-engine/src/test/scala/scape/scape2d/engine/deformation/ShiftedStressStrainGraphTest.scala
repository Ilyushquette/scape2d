package scape.scape2d.engine.deformation

import org.junit.Assert
import org.junit.Test

class ShiftedStressStrainGraphTest {
  @Test
  def testStressForStretchResolution = {
    val linearGraph = LinearStressStrainGraph(5);
    val shiftedGraph = ShiftedStressStrainGraph(linearGraph, 2);
    Assert.assertEquals(30, shiftedGraph.forStrain(4), 0.00001);
  }
  
  @Test
  def testStressForCompressionResolution = {
    val linearGraph = LinearStressStrainGraph(5);
    val shiftedGraph = ShiftedStressStrainGraph(linearGraph, 2);
    Assert.assertEquals(-30, shiftedGraph.forStrain(-4), 0.00001);
  }
}