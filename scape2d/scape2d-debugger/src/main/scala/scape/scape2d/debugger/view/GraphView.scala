package scape.scape2d.debugger.view

import scala.collection.immutable.NumericRange

trait GraphView {
  def render(fx:Double => Double, range:NumericRange[Double]);
  
  def clear(fx:Double => Double, range:NumericRange[Double]);
}