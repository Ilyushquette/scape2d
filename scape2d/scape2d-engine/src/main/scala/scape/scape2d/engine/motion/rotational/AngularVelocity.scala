package scape.scape2d.engine.motion.rotational

import java.lang.Math.abs
import java.lang.Math.signum

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Epsilon
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

object AngularVelocity {
  val zero = Angle.zero / Second;
  
  def apply(angle:Angle, time:Duration) = new AngularVelocity(angle.unbound, time);
}

case class AngularVelocity(angle:UnboundAngle, time:Duration) extends Ordered[AngularVelocity] {
  lazy val radiansPerMillisecond = angle.radians / time.milliseconds;
  
  def forAngle(angle:UnboundAngle) = time * (angle / this.angle);
  
  def forTime(time:Duration) = angle * (time / this.time);
  
  def +(angularVelocity:AngularVelocity) = {
    val angleToAdd = angularVelocity.forTime(time);
    AngularVelocity(angle + angleToAdd, time);
  }
  
  def -(angularVelocity:AngularVelocity) = {
    val angleToSubtract = angularVelocity.forTime(time);
    AngularVelocity(angle - angleToSubtract, time);
  }
  
  def compare(angularVelocity:AngularVelocity) = {
    fuzzyCompare(radiansPerMillisecond, angularVelocity.radiansPerMillisecond, Epsilon);
  }
  
  override def equals(any:Any) = any match {
    case angularVelocity:AngularVelocity =>
      fuzzyEquals(radiansPerMillisecond, angularVelocity.radiansPerMillisecond, Epsilon);
    case _ => false;
  }
  
  def toLinearVelocity(center:Point, point:Point) = {
    val magnitude = abs(angle.radians * (center distanceTo point));
    val direction = (center angleTo point) + (Angle.right * signum(angle.value));
    Vector(magnitude, direction) / time;
  }
}