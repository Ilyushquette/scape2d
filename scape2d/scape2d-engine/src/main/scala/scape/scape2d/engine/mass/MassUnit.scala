package scape.scape2d.engine.mass

object MassUnit {
  implicit def toMass(unit:MassUnit) = Mass(1, unit);
}

sealed trait MassUnit {
  def kilograms:Double;
}

case object Ton extends MassUnit {
  val kilograms = 1000d;
}

case object Kilogram extends MassUnit {
  val kilograms = 1d;
}

case object Gram extends MassUnit {
  val kilograms = 0.001;
}

case object Milligram extends MassUnit {
  val kilograms = Gram.kilograms / 1000;
}