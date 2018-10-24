package scape.scape2d.engine.geom

import java.lang.Math.abs
import java.lang.Math.hypot
import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.angle.cos
import scape.scape2d.engine.geom.angle.sin
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.motion.linear.Velocity

object Vector {
  val zero = Vector(0, Angle.zero);
  
  def from(components:Components) = {
    val magnitude = hypot(components.x, components.y);
    Vector(magnitude, Angle.from(components));
  }
  
  def unit(angle:Angle) = Vector(1, angle);
  
  implicit def toComponents(vector:Vector) = vector.components;
}

case class Vector(magnitude:Double, angle:Angle) extends Ordered[Vector] {
  lazy val components = {
    val offsetX = magnitude * cos(angle);
    val offsetY = magnitude * sin(angle);
    Components(offsetX, offsetY);
  }
  
  // though Vector is implicitly converted to Components,
  // x cannot be accessed through conversion due to naming overlap with cross-product method
  lazy val x = components.x;
  
  lazy val opposite = Vector(magnitude, angle.opposite);
  
  def +(vector:Vector) = Vector.from(Components(this.x + vector.x, this.y + vector.y));
  
  def -(vector:Vector) = Vector.from(Components(this.x - vector.x, this.y - vector.y));
  
  def *(vector:Vector) = this.x * vector.x + this.y * vector.y;
  
  def *(magnitudeMultiplier:Double) = {
    val newMagnitude = magnitude * abs(magnitudeMultiplier);
    val newAngle = if(magnitudeMultiplier >= 0) angle else angle.opposite;
    Vector(newMagnitude, newAngle);
  }
  
  def /(magnitudeDivider:Double) = {
    val newMagnitude = magnitude / abs(magnitudeDivider);
    val newAngle = if(magnitudeDivider >= 0) angle else angle.opposite;
    Vector(newMagnitude, newAngle);
  }
  
  def /(duration:Duration) = Velocity(this, duration);
  
  def x(vector:Vector):Double = this.x * vector.y - this.y * vector.x;
  
  def scalarProjection(target:Vector) = (this * target) / (target.magnitude * target.magnitude);
  
  def projection(target:Vector) = target * scalarProjection(target);
  
  def compare(vector:Vector) = magnitude compareTo vector.magnitude;
  
  override def equals(a:Any) = a match {
    case Vector(omagnitude, oangle) =>
      (fuzzyEquals(magnitude, 0, Epsilon) && fuzzyEquals(omagnitude, 0, Epsilon)) ||
      (fuzzyEquals(magnitude, omagnitude, Epsilon) && angle == oangle);
    case _ => false;
  }
}