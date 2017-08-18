package scape.scape2d.engine.deformation

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic

class DeformationDescriptorTest {
  @Test
  def testStressMagnitudeDeformationDescriptorStretchedInElasticRange = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    val deformation = deformationDescriptor.strained(1.5);
    Assert.assertEquals(3, deformation.stress, 0.00001);
  }
  
  @Test
  def testStressDeformationDescriptorCompressedInElasticRange = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    val deformation = deformationDescriptor.strained(-1.5);
    Assert.assertEquals(-3, deformation.stress, 0.00001);
  }
  
  @Test
  def testNotEvolvedDeformationDescriptorStrainedInElasticRange = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    val deformation = deformationDescriptor.strained(1.5);
    Assert.assertEquals(deformationDescriptor, deformation.evolvedDescriptor);
  }
  
  @Test
  def testElasticEvolutionDeformationDescriptorStretchedInPlasticRange = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    val deformation = deformationDescriptor.strained(4);
    val evolvedElastic = deformation.evolvedDescriptor.elastic;
    Assert.assertEquals(Elastic(LinearStressStrainGraph(2.6666666666666665), 3), evolvedElastic);
  }
  
  @Test
  def testPlasticEvolutionDeformationDescriptorCompressedInPlasticRange = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    val deformation = deformationDescriptor.strained(-4);
    val evolvedPlastic = deformation.evolvedDescriptor.plastic;
    val expectedShiftedGraph = ShiftedStressStrainGraph(LinearStressStrainGraph(2), 1);
    Assert.assertEquals(Plastic(expectedShiftedGraph, 4), evolvedPlastic);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testOutOfElasticOrPlasticRange:Unit = {
    val elastic = new Elastic(LinearStressStrainGraph(2), 3);
    val plastic = new Plastic(LinearStressStrainGraph(2), 5);
    val deformationDescriptor = new DeformationDescriptor(elastic, plastic);
    deformationDescriptor.strained(5.1);
  }
}