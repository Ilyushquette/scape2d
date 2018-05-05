package scape.scape2d.engine.geom.angle

sealed trait AngleUnit {
  def radians:Double;
  
  def upperBound:Double;
}