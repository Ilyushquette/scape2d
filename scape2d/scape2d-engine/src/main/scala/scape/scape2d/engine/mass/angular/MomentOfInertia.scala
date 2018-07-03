package scape.scape2d.engine.mass.angular

import scape.scape2d.engine.mass.Mass

object MomentOfInertia {
  def apply(mass:Mass, distanceToCenter:Double) = {
    new MomentOfInertia(distanceToCenter * distanceToCenter * mass.kilograms);
  }
}

case class MomentOfInertia private[MomentOfInertia](value:Double);