package scape.scape2d.engine.time

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

object Duration {
  val zero = 0(Second);
}

case class Duration(value:Double, unit:TimeUnit) extends Ordered[Duration] {
  lazy val milliseconds = value * unit.milliseconds;
  
  def to(anotherUnit:TimeUnit) = {
    if(unit != anotherUnit) Duration(unit / anotherUnit * value, anotherUnit);
    else this;
  }
  
  def +(duration:Duration) = {
    val convertedDuration = duration to unit;
    copy(value = value + convertedDuration.value);
  }
  
  def -(duration:Duration) = {
    val convertedDuration = duration to unit;
    copy(value = value - convertedDuration.value);
  }
  
  def *(multiplier:Double) = copy(value = value * multiplier);
  
  def /(duration:Duration) = milliseconds / duration.milliseconds;
  
  def /(divider:Double) = copy(value = value / divider);
  
  def compare(duration:Duration) = fuzzyCompare(milliseconds, duration.milliseconds, Epsilon);
  
  override def equals(any:Any) = any match {
    case duration:Duration =>
      fuzzyEquals(milliseconds, duration.milliseconds, Epsilon);
    case unit:TimeUnit =>
      this == TimeUnit.toDuration(unit);
    case _ => false;
  }
}