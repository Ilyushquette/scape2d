package scape.scape2d.engine.geom

import java.lang.Math._
import com.google.common.math.DoubleMath._

object Vector {
  def from(components:Components2D) = {
    val magnitude = hypot(components.x, components.y);
    val radians = atan2(components.y, components.x);
    val angle = normalizeAngle(toDegrees(radians));
    Vector(magnitude, angle);
  }
}

case class Vector(magnitude:Double = 0, angle:Double = 0) {
  lazy val components = {
    val radians = toRadians(angle);
    val offsetX = magnitude * cos(radians);
    val offsetY = magnitude * sin(radians);
    Components2D(offsetX, offsetY);
  }
  
  lazy val opposite = Vector(magnitude, normalizeAngle(angle + 180));
  
  def +(vector:Vector) = mergeWith(vector, (v1, v2) => (v1.x + v2.x, v1.y + v2.y));
  
  def -(vector:Vector) = mergeWith(vector, (v1, v2) => (v1.x - v2.x, v1.y - v2.y));
  
  def *(vector:Vector) = components.x * vector.components.x + components.y * vector.components.y;
  
  def *(magnitudeMultiplier:Double) = {
    val newMagnitude = magnitude * abs(magnitudeMultiplier);
    val newAngle = if(magnitudeMultiplier >= 0) angle else normalizeAngle(angle + 180);
    Vector(newMagnitude, newAngle);
  }
  
  def x(vector:Vector) = components.x * vector.components.y - components.y * vector.components.x;
  
  def scalarProjection(target:Vector) = (this * target) / (target.magnitude * target.magnitude);
  
  def projection(target:Vector) = target * scalarProjection(target);
  
  private def mergeWith(vector:Vector, merge:(Components2D, Components2D) => (Double, Double)) = {
    val merged = merge(components, vector.components);
    Vector.from(Components2D(merged._1, merged._2));
  }
  
  override def hashCode = magnitude.hashCode + angle.hashCode;
  
  override def equals(a:Any) = a match {
    case vector:Vector =>
      (fuzzyEquals(magnitude, 0, Epsilon) && fuzzyEquals(vector.magnitude, 0, Epsilon)) ||
      (fuzzyEquals(magnitude, vector.magnitude, Epsilon) && fuzzyEquals(angle, vector.angle, Epsilon));
    case _ => false;
  }
  
  override def toString = "Vector2D [magnitude=%f, angle=%f]".format(magnitude, angle);
}