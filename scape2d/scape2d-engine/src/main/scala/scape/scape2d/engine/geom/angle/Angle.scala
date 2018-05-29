package scape.scape2d.engine.geom.angle

import java.lang.Math.atan2

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.geom.Components

object Angle {
  val straight = 3.14159265358979(Radian);
  val full = straight * 2;
  val right = straight / 2;
  val zero = 0(Radian);
  
  def bound(value:Double, unit:AngleUnit) = {
    val normalizedValue = (value + unit.upperBound) % unit.upperBound;
    Angle(normalizedValue, unit);
  }
  
  def from(components:Components) = {
    val value = atan2(components.y, components.x);
    Angle.bound(value, Radian);
  }
}

case class Angle private[Angle](value:Double, unit:AngleUnit) extends Ordered[Angle] {
  lazy val radians = value * unit.radians;
  lazy val opposite = this + Angle.straight;
  
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
  
  def compare(angle:Angle) = fuzzyCompare(radians, angle.radians, Epsilon);
  
  override def equals(any:Any) = any match {
    case angle:Angle => fuzzyEquals(radians, angle.radians, Epsilon);
    case unit:AngleUnit => this == AngleUnit.toAngle(unit);
    case _ => false;
  }
  
  lazy val unbound = UnboundAngle(value, unit);
}