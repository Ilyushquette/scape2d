package scape.scape2d.engine

import java.lang.Math._
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import com.google.common.math.DoubleMath._;

package object geom {
  val Epsilon = 1E-10;
  
  def normalizeAngle(angle:Double) = (angle + 360) % 360;
  
  def sinDeg(angle:Double) = sin(toRadians(normalizeAngle(angle)));
  
  def cosDeg(angle:Double) = cos(toRadians(normalizeAngle(angle)));
  
  def findMutualX(l1:Line, l2:Line) = {
    if(!l1.vertical && !l2.vertical && !fuzzyEquals(l1.slope.get, l2.slope.get, Epsilon)) {
      (l2.yIntercept.get - l1.yIntercept.get) / (l1.slope.get - l2.slope.get);
    }
    else if(!l1.vertical && l2.vertical) l2.p1.x;
    else if(l1.vertical && !l2.vertical) l1.p1.x;
    else throw new ArithmeticException("Mutual X for parallel lines has no or infinite solutions");
  }
  
  def findMutualPoint(l1:Line, l2:Line) = {
    val mutualX = findMutualX(l1, l2);
    val mutualY = if(!l1.vertical) l1.forX(mutualX) else l2.forX(mutualX);
    Point(mutualX, mutualY);
  }
}