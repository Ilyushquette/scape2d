package scape.scape2d.engine.mass

private[mass] case class DoubleMass(value:Double) {
  def apply(mass:Mass) = mass.copy(value = value * mass.value);
}