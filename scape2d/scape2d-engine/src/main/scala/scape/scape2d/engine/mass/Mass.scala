package scape.scape2d.engine.mass

case class Mass(value:Double, unit:MassUnit) {
  lazy val kilograms = value * unit.kilograms;
}