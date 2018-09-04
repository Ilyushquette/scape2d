package scape.scape2d.engine.geom

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals

case class SecondMomentOfArea(value:Double) extends Ordered[SecondMomentOfArea] {
  def +(secondMomentOfArea:SecondMomentOfArea) = SecondMomentOfArea(value + secondMomentOfArea.value);
  
  def -(secondMomentOfArea:SecondMomentOfArea) = SecondMomentOfArea(value - secondMomentOfArea.value);
  
  def *(multiplier:Double) = SecondMomentOfArea(value * multiplier);
  
  def /(divider:Double) = SecondMomentOfArea(value / divider);
  
  override def equals(any:Any) = any match {
    case SecondMomentOfArea(value) =>
      fuzzyEquals(this.value, value, SecondMomentOfAreaEpsilon);
    case _ => false;
  }
  
  def compare(secondMomentOfArea:SecondMomentOfArea) = {
    fuzzyCompare(this.value, secondMomentOfArea.value, SecondMomentOfAreaEpsilon);
  }
}