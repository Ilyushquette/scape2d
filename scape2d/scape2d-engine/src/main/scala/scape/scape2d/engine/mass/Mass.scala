package scape.scape2d.engine.mass

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

case class Mass(value:Double, unit:MassUnit) extends Ordered[Mass] {
  lazy val kilograms = value * unit.kilograms;
  
  def to(anotherUnit:MassUnit) = {
    if(unit != anotherUnit) Mass(unit / anotherUnit * value, anotherUnit);
    else this;
  }
  
  def forForce(force:Vector) = InstantAcceleration(Velocity(force / kilograms, Second));
  
  def +(mass:Mass) = {
    val convertedMass = mass to unit;
    Mass(value + convertedMass.value, unit);
  }
  
  def -(mass:Mass) = {
    val convertedMass = mass to unit;
    Mass(value - convertedMass.value, unit);
  }
  
  def *(multiplier:Double) = Mass(value * multiplier, unit);
  
  def /(mass:Mass) = kilograms / mass.kilograms;
  
  def /(divider:Double) = Mass(value / divider, unit);
  
  def compare(mass:Mass) = fuzzyCompare(kilograms, mass.kilograms, Epsilon);
  
  override def equals(any:Any) = any match {
    case mass:Mass => fuzzyEquals(kilograms, mass.kilograms, Epsilon);
    case unit:MassUnit => this == MassUnit.toMass(unit);
    case _ => false;
  }
}