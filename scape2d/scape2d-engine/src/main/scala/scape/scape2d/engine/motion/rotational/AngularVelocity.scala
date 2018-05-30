package scape.scape2d.engine.motion.rotational

import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Epsilon
import com.google.common.math.DoubleMath.fuzzyEquals

object AngularVelocity {
  def apply(angle:Angle, time:Duration) = new AngularVelocity(angle.unbound, time);
}

case class AngularVelocity(angle:UnboundAngle, time:Duration) {
  lazy val radiansPerMillisecond = angle.radians / time.milliseconds;
  
  override def equals(any:Any) = any match {
    case angularVelocity:AngularVelocity =>
      fuzzyEquals(radiansPerMillisecond, angularVelocity.radiansPerMillisecond, Epsilon);
    case _ => false;
  }
}