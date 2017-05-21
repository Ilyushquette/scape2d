package scape.scape2d.engine.geom

import java.lang.Math._;

object Vector2D {
  def from(components:Components2D) = {
    val magnitude = hypot(components.x, components.y);
    val radians = atan2(components.y, components.x);
    val angle = normalizeAngle(toDegrees(radians));
    new Vector2D(magnitude, angle);
  }
}

class Vector2D(var magnitude:Double, var angle:Double) {
  def this() = this(0, 0);
  
  def components = {
    val radians = toRadians(angle);
    val offsetX = magnitude * cos(radians);
    val offsetY = magnitude * sin(radians);
    Components2D(offsetX, offsetY);
  }
  
  def +(vector:Vector2D) = mergeWith(vector, (v1, v2) => (v1.x + v2.x, v1.y + v2.y));
  
  def -(vector:Vector2D) = mergeWith(vector, (v1, v2) => (v1.x - v2.x, v1.y - v2.y));
  
  def *(vector:Vector2D) = {
    val comps = components;
    val otherComps = vector.components;
    comps.x * otherComps.x + comps.y * otherComps.y;
  }
  
  private def mergeWith(vector:Vector2D, merge:(Components2D, Components2D) => (Double, Double)) = {
    val merged = merge(components, vector.components);
    Vector2D.from(Components2D(merged._1, merged._2));
  }
  
  override def hashCode = magnitude.hashCode + angle.hashCode;
  
  override def equals(a:Any) = a match {
    case vector:Vector2D => magnitude == vector.magnitude && angle == vector.angle;
    case _ => false;
  }
  
  override def toString = "Vector2D [magnitude=%f, angle=%f]".format(magnitude, angle);
}