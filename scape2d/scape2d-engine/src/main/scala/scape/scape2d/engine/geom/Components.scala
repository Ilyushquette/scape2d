package scape.scape2d.engine.geom

import java.lang.Math.round

case class Components(x:Double, y:Double) {
  lazy val toInt = Components2DInteger(round(x).toInt, round(y).toInt);
}