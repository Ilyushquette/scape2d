package scape.scape2d.engine.geom.angle

import com.google.common.math.DoubleMath.fuzzyEquals

object Angle {
  val straight = 3.14159265358979(Radian);
  
  def bound(value:Double, unit:AngleUnit) = {
    val normalizedValue = (value + unit.upperBound) % unit.upperBound;
    Angle(normalizedValue, unit);
  }
}

case class Angle private[Angle](value:Double, unit:AngleUnit) {
  lazy val radians = value * unit.radians;
  
  def to(anotherUnit:AngleUnit) = {
    if(unit != anotherUnit) Angle.bound(unit / anotherUnit * value, anotherUnit);
    else this;
  }
  
  def +(angle:Angle) = {
    val convertedAngle = angle to unit;
    Angle.bound(value + convertedAngle.value, unit);
  }
  
  def -(angle:Angle) = {
    val convertedAngle = angle to unit;
    Angle.bound(value - convertedAngle.value, unit);
  }
  
  def *(multiplier:Double) = Angle.bound(value * multiplier, unit);
  
  def /(angle:Angle) = radians / angle.radians;
  
  def /(divider:Double) = Angle.bound(value / divider, unit);
  
  override def equals(any:Any) = any match {
    case angle:Angle => fuzzyEquals(radians, angle.radians, Epsilon);
    case unit:AngleUnit => this == AngleUnit.toAngle(unit);
    case _ => false;
  }
}