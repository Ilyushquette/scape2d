package scape.scape2d.engine.geom.angle

object Angle {
  def bound(value:Double, unit:AngleUnit) = {
    val normalizedValue = (value + unit.upperBound) % unit.upperBound;
    Angle(normalizedValue, unit);
  }
}

case class Angle private[Angle](value:Double, unit:AngleUnit) {
  lazy val radians = value * unit.radians;
}