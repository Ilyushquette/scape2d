package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.angle.DoubleAngle

package object angle {
  val Epsilon = 1E-10;
  
  implicit def doubleToAngle(value:Double) = DoubleAngle(value);
}