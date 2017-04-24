package scape.scape2d.engine

package object geom {
  def calculateComponents(vector:Vector2D) = {
    val radians = Math.toRadians(vector.angle);
    val offsetX = vector.magnitude * Math.cos(radians);
    val offsetY = vector.magnitude * Math.sin(radians);
    Components2D(offsetX, offsetY);
  }
}