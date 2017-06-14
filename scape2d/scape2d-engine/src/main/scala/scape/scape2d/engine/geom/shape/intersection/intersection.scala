package scape.scape2d.engine.geom.shape

import java.lang.Math._

import com.google.common.math.DoubleMath

package object intersection {
  def testIntersection(p1:Point, p2:Point) = p1 == p2;
  
  def testIntersection(line:Line, point:Point):Boolean = {
    if(line.vertical) DoubleMath.fuzzyEquals(point.x, line.forY(point.y), 0.00001);
    else DoubleMath.fuzzyEquals(point.y, line.forX(point.x), 0.00001);
  }
  
  def testIntersection(l1:Line, l2:Line):Boolean = {
    if(!l1.vertical && !l2.vertical) l1.slope.get != l2.slope.get || l1.yIntercept.get == l2.yIntercept.get;
    else if(l1.vertical && l2.vertical) l1.p1.x == l2.p1.x;
    else true;
  }
  
  def testIntersection(segment:Segment, point:Point):Boolean = {
    val mutualAxes = point.x >= min(segment.p1.x, segment.p2.x) &&
                     point.x <= max(segment.p1.x, segment.p2.x) &&
                     point.y >= min(segment.p1.y, segment.p2.y) &&
                     point.y <= max(segment.p1.y, segment.p2.y);
    
    mutualAxes && segment.line.intersects(point);
  }
}