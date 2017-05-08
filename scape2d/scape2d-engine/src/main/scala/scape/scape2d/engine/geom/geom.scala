package scape.scape2d.engine

package object geom {
  def normalizeAngle(angle:Double) = (angle + 360) % 360;
}