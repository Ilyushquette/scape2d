package scape.scape2d.engine.deformation.elasticity

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.deformation.LinearStressStrainGraph

class ElasticTest {
  @Test
  def testHardenedElasticLimit = {
    val elastic = new Elastic(LinearStressStrainGraph(1), 3);
    val hardenedElastic = elastic.hardened(4);
    Assert.assertEquals(elastic.limit, hardenedElastic.limit, 0.00001);
  }
  
  @Test
  def testHardenedElasticStiffness = {
    val elastic = new Elastic(LinearStressStrainGraph(1), 3);
    val hardenedElastic = elastic.hardened(4);
    Assert.assertEquals(1.33333, hardenedElastic.graph.stiffness, 0.00001);
  }
}