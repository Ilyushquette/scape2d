package scape.scape2d.engine.geom

object Point2D {
  def origin = new Point2D(0, 0);
}

class Point2D(var x:Double, var y:Double) {
  override def clone = new Point2D(x, y);
  
  override def hashCode = x.hashCode + y.hashCode;
  
  override def equals(a:Any) = a match {
    case point:Point2D => x == point.x && y == point.y
    case _ => false
  }
  
  override def toString = "Point2D [x=%f, y=%f]".format(x, y);
}