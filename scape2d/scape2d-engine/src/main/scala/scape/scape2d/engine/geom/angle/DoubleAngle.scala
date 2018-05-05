package scape.scape2d.engine.geom.angle

private[angle] case class DoubleAngle(value:Double) {
  def apply(angle:Angle) = Angle.bound(value * angle.value, angle.unit);
}