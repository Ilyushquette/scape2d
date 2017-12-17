package scape.scape2d.engine.time

import com.google.common.math.DoubleMath

case class Duration(value:Double, unit:TimeUnit) {
  lazy val milliseconds = value * unit.milliseconds;
  
  override def equals(any:Any) = any match {
    case duration:Duration =>
      DoubleMath.fuzzyEquals(milliseconds, duration.milliseconds, Epsilon);
    case unit:TimeUnit =>
      this == TimeUnit.toDuration(unit);
    case _ => false;
  }
}