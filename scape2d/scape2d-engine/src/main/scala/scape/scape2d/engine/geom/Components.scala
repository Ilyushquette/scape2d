package scape.scape2d.engine.geom

import java.lang.Math.round
import com.google.common.math.DoubleMath.fuzzyEquals

case class Components(x:Double, y:Double) {
  override def equals(any:Any) = any match {
    case Components(ox, oy) =>
      fuzzyEquals(x, ox, Epsilon) && fuzzyEquals(y, oy, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = ComponentsInteger(round(x).toInt, round(y).toInt);
}