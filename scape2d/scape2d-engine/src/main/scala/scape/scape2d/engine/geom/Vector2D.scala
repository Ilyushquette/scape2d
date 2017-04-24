package scape.scape2d.engine.geom

class Vector2D(var magnitude:Double, var angle:Double) {
  def this() = this(0, 0);
  override def toString = "Vector2D [magnitude=%f, angle=%f]".format(magnitude, angle);
}