package scape.scape2d.engine

import java.lang.Math._
import scape.scape2d.engine.geom.shape.Line

package object geom {
  def normalizeAngle(angle:Double) = (angle + 360) % 360;
  
  def sinDeg(angle:Double) = sin(toRadians(normalizeAngle(angle)));
  
  def cosDeg(angle:Double) = cos(toRadians(normalizeAngle(angle)));
  
  def findMutualX(l1:Line, l2:Line) = {
    if(!l1.vertical && !l2.vertical && l1.slope.get != l2.slope.get) {
      (l2.yIntercept.get - l1.yIntercept.get) / (l1.slope.get - l2.slope.get);
    }
    else if(!l1.vertical && l2.vertical) l2.p1.x;
    else if(l1.vertical && !l2.vertical) l1.p1.x;
    else throw new ArithmeticException("Mutual X for parallel lines has no or infinite solutions");
  }
}