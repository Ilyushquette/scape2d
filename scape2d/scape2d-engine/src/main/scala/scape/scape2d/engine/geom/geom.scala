package scape.scape2d.engine

import java.lang.Math
package object geom {
  def calculateComponents(vector:Vector2D) = {
    val radians = Math.toRadians(vector.angle);
    val offsetX = vector.magnitude * Math.cos(radians);
    val offsetY = vector.magnitude * Math.sin(radians);
    Components2D(offsetX, offsetY);
  }
  
  def calculateVector(components:Components2D) = {
    val magnitude = Math.hypot(components.x, components.y);
    val radians = Math.atan2(components.y, components.x);
    val angle = Math.toDegrees(radians);
    new Vector2D(magnitude, angle);
  }
  
  def sum(vectors:Vector2D*) = {
    val foldedComponents = vectors.foldLeft(Components2D(0, 0))((acc, cur) => {
      val components = calculateComponents(cur);
      Components2D(acc.x + components.x, acc.y + components.y);
    });
    calculateVector(foldedComponents);
  }
}