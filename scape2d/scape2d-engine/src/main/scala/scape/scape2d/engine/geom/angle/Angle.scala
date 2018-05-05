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
  
  override def equals(any:Any) = any match {
    case angle:Angle => fuzzyEquals(radians, angle.radians, Epsilon);
    case unit:AngleUnit => this == AngleUnit.toAngle(unit);
    case _ => false;
  }
}