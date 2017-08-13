package scape.scape2d.engine.deformation

import scape.scape2d.engine.geom.Epsilon
import com.google.common.math.DoubleMath

object LinearStressStrainGraph {
  def apply(youngModulus:Double, crossSectionalArea:Double, length:Double) = {
    new LinearStressStrainGraph((youngModulus * crossSectionalArea) / length);
  }
}

/**
 * Units:
 * <ul>
 *  <li>
 *  stiffness - slope of stress over strain.
 *  How much stress in Newtons is required to achieve
 *  1 meter of strain of specific structure of material</li>
 * </ul>
 */
case class LinearStressStrainGraph(stiffness:Double) extends StressStrainGraph {
  def forStrain(strain:Double) = strain * stiffness;
  
  def forStress(stress:Double) = stress / stiffness;
  
  override def equals(any:Any) = any match {
    case LinearStressStrainGraph(ostiffness) => DoubleMath.fuzzyEquals(stiffness, ostiffness, Epsilon);
    case _ => false;
  }
}