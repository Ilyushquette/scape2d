package scape.scape2d.engine.mass.angular

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.mass.Mass

object MomentOfInertia {
  def apply(mass:Mass, distanceToCenter:Double) = {
    new MomentOfInertia(distanceToCenter * distanceToCenter * mass.kilograms);
  }
}

case class MomentOfInertia private[MomentOfInertia](
  value:Double
) extends Ordered[MomentOfInertia] {
  def compare(momentOfInertia:MomentOfInertia) = fuzzyCompare(value, momentOfInertia.value, Epsilon);
  
  override def equals(any:Any) = any match {
    case momentOfInertia:MomentOfInertia => fuzzyEquals(value, momentOfInertia.value, Epsilon);
    case _ => false;
  }
}