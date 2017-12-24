package scape.scape2d.engine.deformation

import java.lang.Math.abs
import java.lang.Math.signum

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Epsilon

case class ShiftedStressStrainGraph(graph:StressStrainGraph, shift:Double) extends StressStrainGraph {
  def forStrain(strain:Double):Double = signum(strain) * graph.forStrain(abs(strain) + shift);

  def forStress(stress:Double):Double = graph.forStress(stress);
  
  override def equals(any:Any) = any match {
    case ShiftedStressStrainGraph(ograph, oshift) =>
      graph == ograph && fuzzyEquals(shift, oshift, Epsilon);
    case _ => false;
  }
}