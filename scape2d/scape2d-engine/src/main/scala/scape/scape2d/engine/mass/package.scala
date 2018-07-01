package scape.scape2d.engine

package object mass {
  val Epsilon = 1E-10;
  
  implicit def doubleToMass(value:Double) = DoubleMass(value);
}