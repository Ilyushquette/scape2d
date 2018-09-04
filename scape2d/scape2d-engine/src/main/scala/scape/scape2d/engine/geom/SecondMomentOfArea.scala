package scape.scape2d.engine.geom

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

case class SecondMomentOfArea(value:Double) extends Ordered[SecondMomentOfArea] {
  override def equals(any:Any) = any match {
    case SecondMomentOfArea(value) =>
      fuzzyEquals(this.value, value, SecondMomentOfAreaEpsilon);
    case _ => false;
  }
  
  def compare(secondMomentOfArea:SecondMomentOfArea) = {
    fuzzyCompare(this.value, secondMomentOfArea.value, SecondMomentOfAreaEpsilon);
  }
}