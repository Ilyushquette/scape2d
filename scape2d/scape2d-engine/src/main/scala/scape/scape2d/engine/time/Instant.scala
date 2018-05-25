package scape.scape2d.engine.time

object Instant {
  def now() = {
    val unixEpochDuration = Duration(System.currentTimeMillis(), Millisecond);
    Instant(unixEpochDuration);
  }
}

case class Instant(unixEpochDuration:Duration);