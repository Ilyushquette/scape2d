package scape.scape2d.graphics

import scape.scape2d.engine.geom.shape.Shape

object CustomizedShape {
  implicit def toShape[S <: Shape](customizedShape:CustomizedShape[S]):S = customizedShape.shape;
}

case class CustomizedShape[S <: Shape](shape:S, rgb:Int, mixColors:Boolean = true);