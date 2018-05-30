package scape.scape2d.engine.geom.angle

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.motion.rotational.AngularVelocity

case class UnboundAngle(value:Double, unit:AngleUnit) extends Ordered[UnboundAngle] {
  lazy val radians = value * unit.radians;
  
  def to(anotherUnit:AngleUnit) = {
    if(unit != anotherUnit) UnboundAngle(unit / anotherUnit * value, anotherUnit);
    else this;
  }
  
  def +(unboundAngle:UnboundAngle):UnboundAngle = {
    val convertedUnboundAngle = unboundAngle to unit;
    UnboundAngle(value + convertedUnboundAngle.value, unit);
  }
  
  def -(unboundAngle:UnboundAngle):UnboundAngle = {
    val convertedUnboundAngle = unboundAngle to unit;
    UnboundAngle(value - convertedUnboundAngle.value, unit);
  }
  
  def *(multiplier:Double) = UnboundAngle(value * multiplier, unit);
  
  def /(unboundAngle:UnboundAngle) = radians / unboundAngle.radians;
  
  def /(divider:Double) = UnboundAngle(value / divider, unit);
  
  def /(duration:Duration) = AngularVelocity(this, duration);
  
  def compare(unboundAngle:UnboundAngle) = fuzzyCompare(radians, unboundAngle.radians, Epsilon);
  
  override def equals(any:Any) = any match {
    case unboundAngle:UnboundAngle => fuzzyEquals(radians, unboundAngle.radians, Epsilon);
    case _ => false;
  }
  
  lazy val bound = Angle.bound(value, unit);
}