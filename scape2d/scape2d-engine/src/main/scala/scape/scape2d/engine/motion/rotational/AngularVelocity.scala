package scape.scape2d.engine.motion.rotational

import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Epsilon
import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.time.Second

object AngularVelocity {
  val zero = Angle.zero / Second;
  
  def apply(angle:Angle, time:Duration) = new AngularVelocity(angle.unbound, time);
}

case class AngularVelocity(angle:UnboundAngle, time:Duration) extends Ordered[AngularVelocity] {
  lazy val radiansPerMillisecond = angle.radians / time.milliseconds;
  
  def compare(angularVelocity:AngularVelocity) = {
    fuzzyCompare(radiansPerMillisecond, angularVelocity.radiansPerMillisecond, Epsilon);
  }
  
  override def equals(any:Any) = any match {
    case angularVelocity:AngularVelocity =>
      fuzzyEquals(radiansPerMillisecond, angularVelocity.radiansPerMillisecond, Epsilon);
    case _ => false;
  }
}