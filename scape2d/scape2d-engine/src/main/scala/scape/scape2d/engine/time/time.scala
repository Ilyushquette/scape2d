package scape.scape2d.engine

package object time {
  val Epsilon = 1E-10;
  
  implicit def doubleToTime(value:Double) = DoubleTime(value);
}