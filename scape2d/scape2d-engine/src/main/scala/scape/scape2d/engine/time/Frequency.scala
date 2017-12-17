package scape.scape2d.engine.time

case class Frequency(occurences:Double, duration:Duration) {
  lazy val occurenceDuration = Duration(duration.value / occurences, duration.unit);
  
  override def equals(any:Any) = any match {
    case other:Frequency => occurenceDuration == other.occurenceDuration;
    case _ => false;
  }
}