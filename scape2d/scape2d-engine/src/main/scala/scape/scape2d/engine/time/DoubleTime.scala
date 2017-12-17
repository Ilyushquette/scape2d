package scape.scape2d.engine.time

private[time] case class DoubleTime(value:Double) {
  def apply(duration:Duration) = duration.copy(value = value * duration.value);
  
  def *(duration:Duration) = duration * value;
}