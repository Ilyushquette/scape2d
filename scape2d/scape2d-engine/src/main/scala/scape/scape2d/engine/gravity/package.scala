package scape.scape2d.engine

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.geom.shape.FiniteShape

package object gravity {
  val G = 6.674E-11;
  
  def gravitationalForceMagnitudeBetween(matter1:Matter[_ <: FiniteShape], matter2:Matter[_ <: FiniteShape]) = {
    val r = matter1.position distanceTo matter2.position;
    if(r != 0) {
      val m1 = matter1.mass.kilograms;
      val m2 = matter2.mass.kilograms;
      G * ((m1 * m2) / (r * r));
    }else 0;
  }
}