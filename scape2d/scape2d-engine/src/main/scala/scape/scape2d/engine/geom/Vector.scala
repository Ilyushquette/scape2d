package scape.scape2d.engine.geom

import java.lang.Math._
import com.google.common.math.DoubleMath._

object Vector {
  def from(components:Components) = {
    val magnitude = hypot(components.x, components.y);
    val radians = atan2(components.y, components.x);
    val angle = normalizeRadians(radians);
    Vector(magnitude, angle);
  }
  
  implicit def toComponents(vector:Vector) = vector.components;
}

case class Vector(magnitude:Double = 0, angle:Double = 0) {
  lazy val components = {
    val offsetX = magnitude * cos(angle);
    val offsetY = magnitude * sin(angle);
    Components(offsetX, offsetY);
  }
  
  // though Vector is implicitly converted to Components,
  // x cannot be accessed through conversion due to naming overlap with cross-product method
  lazy val x = components.x;
  
  lazy val opposite = Vector(magnitude, normalizeRadians(angle + PI));
  
  def +(vector:Vector) = Vector.from(Components(this.x + vector.x, this.y + vector.y));
  
  def -(vector:Vector) = Vector.from(Components(this.x - vector.x, this.y - vector.y));
  
  def *(vector:Vector) = this.x * vector.x + this.y * vector.y;
  
  def *(magnitudeMultiplier:Double) = {
    val newMagnitude = magnitude * abs(magnitudeMultiplier);
    val newAngle = if(magnitudeMultiplier >= 0) angle else normalizeRadians(angle + PI);
    Vector(newMagnitude, newAngle);
  }
  
  def x(vector:Vector):Double = this.x * vector.y - this.y * vector.x;
  
  def scalarProjection(target:Vector) = (this * target) / (target.magnitude * target.magnitude);
  
  def projection(target:Vector) = target * scalarProjection(target);
  
  override def equals(a:Any) = a match {
    case Vector(omagnitude, oangle) =>
      (fuzzyEquals(magnitude, 0, Epsilon) && fuzzyEquals(omagnitude, 0, Epsilon)) ||
      (fuzzyEquals(magnitude, omagnitude, Epsilon) && fuzzyEquals(angle, oangle, Epsilon));
    case _ => false;
  }
}