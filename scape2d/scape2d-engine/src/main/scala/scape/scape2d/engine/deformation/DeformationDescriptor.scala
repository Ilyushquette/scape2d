package scape.scape2d.engine.deformation

import java.lang.Math.abs
import java.lang.Math.signum

import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic

case class DeformationDescriptor(elastic:Elastic, plastic:Plastic) {
  def strained(strain:Double):Deformation = {
    val absoluteStrain = abs(strain);
    if(absoluteStrain <= elastic.limit) {
      val stress = elastic.graph.forStrain(strain);
      Deformation(strain, stress, this);
    }else if(absoluteStrain <= plastic.limit) {
      val plasticStrain = absoluteStrain - elastic.limit;
      val stress = plastic.graph.forStrain(strain);
      val evolvedElastic = elastic.hardened(stress);
      val evolvedPlastic = plastic.strained(plasticStrain);
      val evolvedDescriptor = new DeformationDescriptor(evolvedElastic, evolvedPlastic);
      // recursive invocation on strain-adapted deformation descriptor
      evolvedDescriptor.strained(signum(strain) * elastic.limit);
    }else throw new IllegalArgumentException("Strain neither in elastic nor in plastic range");
  }
}