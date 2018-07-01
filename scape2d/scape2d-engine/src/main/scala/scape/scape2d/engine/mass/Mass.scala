package scape.scape2d.engine.mass

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

case class Mass(value:Double, unit:MassUnit) extends Ordered[Mass] {
  lazy val kilograms = value * unit.kilograms;
  
  def compare(mass:Mass) = fuzzyCompare(kilograms, mass.kilograms, Epsilon);
  
  override def equals(any:Any) = any match {
    case mass:Mass => fuzzyEquals(kilograms, mass.kilograms, Epsilon);
    case unit:MassUnit => this == MassUnit.toMass(unit);
    case _ => false;
  }
}