package scape.scape2d.engine.time

object Instant {
  def now() = {
    val unixEpochDuration = Duration(System.currentTimeMillis(), Millisecond);
    Instant(unixEpochDuration);
  }
}

case class Instant(unixEpochDuration:Duration) extends Ordered[Instant] {
  def +(duration:Duration) = Instant(unixEpochDuration + duration);
  
  def -(duration:Duration) = Instant(unixEpochDuration - duration);
  
  def -(instant:Instant) = unixEpochDuration - instant.unixEpochDuration;
  
  def compare(instant:Instant) = unixEpochDuration compareTo instant.unixEpochDuration;
  
  override def equals(any:Any) = any match {
    case Instant(otherUnixEpochDuration) => unixEpochDuration == otherUnixEpochDuration;
    case _ => false;
  }
}