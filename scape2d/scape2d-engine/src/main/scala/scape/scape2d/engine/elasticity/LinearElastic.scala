package scape.scape2d.engine.elasticity

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point

object LinearElastic {
  def apply(youngModulus:Double, crossSectionalArea:Double, length:Double) = {
    new LinearElastic((youngModulus * crossSectionalArea) / length);
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
case class LinearElastic(val stiffness:Double) {
  def forStrain(strain:Double) = strain * stiffness;
  
  def forStress(stress:Double) = stress / stiffness;
}