package scape.scape2d.engine.geom.angle

case class UnboundAngle(value:Double, unit:AngleUnit) {
  lazy val radians = value * unit.radians;
  
  lazy val bound = Angle.bound(value, unit);
}