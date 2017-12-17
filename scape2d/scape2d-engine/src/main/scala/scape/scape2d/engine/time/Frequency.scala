package scape.scape2d.engine.time

case class Frequency(occurences:Double, duration:Duration) {
  lazy val occurenceDuration = Duration(duration.value / occurences, duration.unit);
}