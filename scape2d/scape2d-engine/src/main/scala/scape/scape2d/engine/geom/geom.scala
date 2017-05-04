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
    val angle = normalizeAngle(Math.toDegrees(radians));
    new Vector2D(magnitude, angle);
  }
  
  private def foldComponents(init:Components2D)(vectors:Iterable[Vector2D], op:(Double, Double) => Double) = {
    vectors match {
      case Nil => init;
      case vs => vs.foldLeft(init)((acc, cur) => {
        val components = calculateComponents(cur);
        Components2D(op(acc.x, components.x), op(acc.y, components.y));
      });
    }
  }
  
  def sum(vectors:Iterable[Vector2D]) = {
    val componentsSum = foldComponents(Components2D(0, 0))(vectors, _ + _);
    calculateVector(componentsSum);
  }
  
  def subtract(vectors:Iterable[Vector2D]) = {
    val initial = calculateComponents(vectors.head);
    val componentSubtraction = foldComponents(initial)(vectors.tail, _ - _);
    calculateVector(componentSubtraction);
  }
  
  private def normalizeAngle(angle:Double) = (angle + 360) % 360;
}