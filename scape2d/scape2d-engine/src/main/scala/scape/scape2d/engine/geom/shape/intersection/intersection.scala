package scape.scape2d.engine.geom.shape

import com.google.common.math.DoubleMath

package object intersection {
  def testIntersection(p1:Point, p2:Point) = p1 == p2;
  
  def testIntersection(line:Line, point:Point) = {
    if(line.vertical) DoubleMath.fuzzyEquals(point.x, line.forY(point.y), 0.00001);
    else DoubleMath.fuzzyEquals(point.y, line.forX(point.x), 0.00001);
  }
  
  def testIntersection(l1:Line, l2:Line) = {
    if(!l1.vertical && !l2.vertical) l1.slope.get != l2.slope.get || l1.yIntercept.get == l2.yIntercept.get;
    else if(l1.vertical && l2.vertical) l1.p1.x == l2.p1.x;
    else true;
  }
}