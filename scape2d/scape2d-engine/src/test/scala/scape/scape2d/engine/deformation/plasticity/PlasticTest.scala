package scape.scape2d.engine.deformation.plasticity

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.ShiftedStressStrainGraph

class PlasticTest {
  @Test(expected=classOf[IllegalArgumentException])
  def testPlasticStrainFracture:Unit = {
    val plastic = new Plastic(LinearStressStrainGraph(1), 10);
    plastic.strained(11);
  }
  
  @Test
  def testStrainedPlasticLimit = {
    val plastic = new Plastic(LinearStressStrainGraph(1), 10);
    val strainedPlastic = plastic.strained(2);
    Assert.assertEquals(8, strainedPlastic.limit, 0.00001);
  }
  
  @Test
  def testStrainedPlasticPermanentStrain = {
    val plastic = new Plastic(LinearStressStrainGraph(1), 10);
    val strainedPlastic = plastic.strained(2);
    val shiftedStressStrainGraph = strainedPlastic.graph.asInstanceOf[ShiftedStressStrainGraph];
    Assert.assertEquals(2, shiftedStressStrainGraph.shift, 0.00001);
  }
}