package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.angle.DoubleAngle

package object angle {
  val Epsilon = 1E-10;
  
  implicit def doubleToAngle(value:Double) = DoubleAngle(value);
  
  def cos(angle:Angle) = java.lang.Math.cos(angle.radians);
  
  def sin(angle:Angle) = java.lang.Math.sin(angle.radians);
}