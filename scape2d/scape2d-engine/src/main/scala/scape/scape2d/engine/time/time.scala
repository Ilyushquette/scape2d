package scape.scape2d.engine

package object time {
  implicit def doubleToTime(value:Double) = DoubleTime(value);
}