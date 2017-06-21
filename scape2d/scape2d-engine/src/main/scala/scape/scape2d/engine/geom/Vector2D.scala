package scape.scape2d.engine.geom

import java.lang.Math._
import com.google.common.math.DoubleMath._

object Vector2D {
  def from(components:Components2D) = {
    val magnitude = hypot(components.x, components.y);
    val radians = atan2(components.y, components.x);
    val angle = normalizeAngle(toDegrees(radians));
    new Vector2D(magnitude, angle);
  }
}

class Vector2D(val magnitude:Double, val angle:Double) {
  lazy val components = {
    val radians = toRadians(angle);
    val offsetX = magnitude * cos(radians);
    val offsetY = magnitude * sin(radians);
    Components2D(offsetX, offsetY);
  }
  
  def this() = this(0, 0);
  
  def +(vector:Vector2D) = mergeWith(vector, (v1, v2) => (v1.x + v2.x, v1.y + v2.y));
  
  def -(vector:Vector2D) = mergeWith(vector, (v1, v2) => (v1.x - v2.x, v1.y - v2.y));
  
  def *(vector:Vector2D) = components.x * vector.components.x + components.y * vector.components.y;
  
  def *(magnitudeMultiplier:Double) = {
    val newMagnitude = magnitude * abs(magnitudeMultiplier);
    val newAngle = if(magnitudeMultiplier >= 0) angle else normalizeAngle(angle + 180);
    new Vector2D(newMagnitude, newAngle);
  }
  
  def scalarProjection(target:Vector2D) = (this * target) / (target.magnitude * target.magnitude);
  
  def projection(target:Vector2D) = target * scalarProjection(target);
  
  def opposite = new Vector2D(magnitude, normalizeAngle(angle + 180));
  
  private def mergeWith(vector:Vector2D, merge:(Components2D, Components2D) => (Double, Double)) = {
    val merged = merge(components, vector.components);
    Vector2D.from(Components2D(merged._1, merged._2));
  }
  
  override def hashCode = magnitude.hashCode + angle.hashCode;
  
  override def equals(a:Any) = a match {
    case vector:Vector2D => fuzzyEquals(magnitude, vector.magnitude, Epsilon) &&
                            fuzzyEquals(angle, vector.angle, Epsilon);
    case _ => false;
  }
  
  override def toString = "Vector2D [magnitude=%f, angle=%f]".format(magnitude, angle);
}