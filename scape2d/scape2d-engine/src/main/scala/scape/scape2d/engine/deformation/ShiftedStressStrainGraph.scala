package scape.scape2d.engine.deformation

import java.lang.Math.abs
import java.lang.Math.signum

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Epsilon

case class ShiftedStressStrainGraph(deformation:StressStrainGraph, shift:Double) extends StressStrainGraph {
  def forStrain(strain:Double):Double = signum(strain) * deformation.forStrain(abs(strain) + shift);

  def forStress(stress:Double):Double = deformation.forStress(stress);
  
  override def equals(any:Any) = any match {
    case ShiftedStressStrainGraph(odeformation, oshift) =>
      deformation == odeformation && fuzzyEquals(shift, oshift, Epsilon);
    case _ => false;
  }
}