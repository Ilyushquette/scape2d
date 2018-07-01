package scape.scape2d.engine

package object mass {
  implicit def doubleToMass(value:Double) = DoubleMass(value);
}