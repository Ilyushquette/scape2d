package scape.scape2d.engine.time

case class Duration(value:Double, unit:TimeUnit) {
  lazy val milliseconds = value * unit.milliseconds;
}