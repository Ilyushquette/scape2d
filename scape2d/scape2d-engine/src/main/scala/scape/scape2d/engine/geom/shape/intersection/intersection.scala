package scape.scape2d.engine.geom.shape

import com.google.common.math.DoubleMath

package object intersection {
  def testIntersection(p1:Point, p2:Point) = p1 == p2;
  
  def testIntersection(line:Line, point:Point) = {
    if(line.vertical) DoubleMath.fuzzyEquals(point.x, line.forY(point.y), 0.00001);
    else DoubleMath.fuzzyEquals(point.y, line.forX(point.x), 0.00001);
  }
}