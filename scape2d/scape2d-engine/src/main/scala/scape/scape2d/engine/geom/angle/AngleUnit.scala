package scape.scape2d.engine.geom.angle

import java.lang.Math.PI

sealed trait AngleUnit {
  def radians:Double;
  
  def upperBound:Double;
}

case object Degree extends AngleUnit {
  val radians = PI / 180;
  val upperBound = 360d;
}

case object Radian extends AngleUnit {
  val radians = 1d;
  val upperBound = PI * 2;
}