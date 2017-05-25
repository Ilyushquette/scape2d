package scape.scape2d.engine

import java.lang.Math._

package object geom {
  def normalizeAngle(angle:Double) = (angle + 360) % 360;
  
  def sinDeg(angle:Double) = sin(toRadians(normalizeAngle(angle)));
  
  def cosDeg(angle:Double) = cos(toRadians(normalizeAngle(angle)));
}