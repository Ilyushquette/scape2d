package scape.scape2d.engine.density

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.AreaEpsilon
import scape.scape2d.engine.mass.Mass

case class Density(mass:Mass, area:Double) extends Ordered[Density] {
  lazy val kilogramsPerSquareMeter = mass.kilograms / area;
  
  def forArea(area:Double) = mass * (area / this.area);
  
  def forMass(mass:Mass) = area * (mass / this.mass);
  
  override def equals(any:Any) = any match {
    case density:Density =>
      fuzzyEquals(kilogramsPerSquareMeter, density.kilogramsPerSquareMeter, AreaEpsilon);
    case _ => false;
  }
  
  def compare(density:Density) = fuzzyCompare(kilogramsPerSquareMeter, density.kilogramsPerSquareMeter, AreaEpsilon);
}