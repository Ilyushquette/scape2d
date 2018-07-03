package scape.scape2d.engine.mass.angular

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.rotational.InstantAngularAcceleration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

object MomentOfInertia {
  def apply(mass:Mass, distanceToCenter:Double) = {
    new MomentOfInertia(distanceToCenter * distanceToCenter * mass.kilograms);
  }
}

case class MomentOfInertia private[MomentOfInertia](
  value:Double
) extends Ordered[MomentOfInertia] {
  def forTorque(torque:Double) = {
    val magnitude = torque / value;
    InstantAngularAcceleration(UnboundAngle(magnitude, Radian) / Second);
  }
  
  def forAngularAcceleration(instantAngularAcceleration:InstantAngularAcceleration) = {
    val anglePerSecond = instantAngularAcceleration.angularVelocity.forTime(Second);
    anglePerSecond.radians * value;
  }
  
  def +(momentOfInertia:MomentOfInertia):MomentOfInertia = MomentOfInertia(value + momentOfInertia.value);
  
  def -(momentOfInertia:MomentOfInertia):MomentOfInertia = MomentOfInertia(value - momentOfInertia.value);
  
  def compare(momentOfInertia:MomentOfInertia) = fuzzyCompare(value, momentOfInertia.value, Epsilon);
  
  override def equals(any:Any) = any match {
    case momentOfInertia:MomentOfInertia => fuzzyEquals(value, momentOfInertia.value, Epsilon);
    case _ => false;
  }
}