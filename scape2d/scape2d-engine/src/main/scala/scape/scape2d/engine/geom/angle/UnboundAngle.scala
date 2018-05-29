package scape.scape2d.engine.geom.angle

import com.google.common.math.DoubleMath.fuzzyEquals

case class UnboundAngle(value:Double, unit:AngleUnit) {
  lazy val radians = value * unit.radians;
  
  override def equals(any:Any) = any match {
    case unboundAngle:UnboundAngle => fuzzyEquals(radians, unboundAngle.radians, Epsilon);
    case _ => false;
  }
  
  lazy val bound = Angle.bound(value, unit);
}