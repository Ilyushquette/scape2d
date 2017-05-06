package scape.scape2d.engine

import java.lang.Math
package object geom {
  def normalizeAngle(angle:Double) = (angle + 360) % 360;
}