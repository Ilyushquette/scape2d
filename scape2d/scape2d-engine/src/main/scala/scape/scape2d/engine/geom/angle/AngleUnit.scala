package scape.scape2d.engine.geom.angle

import java.lang.Math.PI

object AngleUnit {
  implicit def toAngle(unit:AngleUnit) = Angle.bound(1, unit);
}

sealed trait AngleUnit {
  def radians:Double;
  
  def upperBound:Double;
  
  def middle:Double;
}

case object Degree extends AngleUnit {
  val radians = PI / 180;
  val upperBound = 360d;
  val middle = 180d;
}

case object Radian extends AngleUnit {
  val radians = 1d;
  val upperBound = PI * 2;
  val middle = PI;
}