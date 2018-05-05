package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.angle.DoubleAngle

package object angle {
  implicit def doubleToAngle(value:Double) = DoubleAngle(value);
}