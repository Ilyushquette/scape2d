package scape.scape2d.engine.geom

class Vector2D(var magnitude:Double, var angle:Double) {
  def this() = this(0, 0);
  
  override def hashCode = magnitude.hashCode + angle.hashCode;
  
  override def equals(a:Any) = a match {
    case vector:Vector2D => magnitude == vector.magnitude && angle == vector.angle;
    case _ => false
  }
  
  override def toString = "Vector2D [magnitude=%f, angle=%f]".format(magnitude, angle);
}