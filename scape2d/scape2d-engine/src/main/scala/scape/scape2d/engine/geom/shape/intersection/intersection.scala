package scape.scape2d.engine.geom.shape

import java.lang.Math._
import scape.scape2d.engine.geom._
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
  
  def testIntersection(s1:Segment, s2:Segment):Boolean = {
    if(s1.line.vertical && s2.line.vertical)
      s1.p1.x == s2.p1.x &&
      max(s1.p1.y, s1.p2.y) >= min(s2.p1.y, s2.p2.y) && // mutual vertical interval
      min(s1.p1.y, s1.p2.y) <= max(s2.p1.y, s2.p2.y);
    else if(!s1.line.vertical && !s2.line.vertical && s1.line.slope.get == s2.line.slope.get)
      s1.line.yIntercept.get == s2.line.yIntercept.get &&
      max(s1.p1.x, s1.p2.x) >= min(s2.p1.x, s2.p2.x) && // mutual horizontal interval
      min(s1.p1.x, s1.p2.x) <= max(s2.p1.x, s2.p2.x);
    else {
      val mutualX = findMutualX(s1.line, s2.line);
      mutualX >= max(min(s1.p1.x, s1.p2.x), min(s2.p1.x, s2.p2.x)) && // lies within mutual abscissa
      mutualX <= min(max(s1.p1.x, s1.p2.x), max(s2.p1.x, s2.p2.x));
    }
  }
  
  def testIntersection(segment:Segment, line:Line):Boolean = {
    if(line.vertical) min(segment.p1.x, segment.p2.x) <= line.p1.x &&
                      max(segment.p1.x, segment.p2.x) >= line.p1.x;
    else if(line.horizontal) min(segment.p1.y, segment.p2.y) <= line.p1.y &&
                             max(segment.p1.y, segment.p2.y) >= line.p1.y;
    else if(segment.line.vertical) segment.intersects(line.clampOrdinate(segment.p1.y, segment.p2.y));
    else segment.intersects(line.clampAbscissa(segment.p1.x, segment.p2.x));
  }
  
  def testIntersection(circle:Circle, point:Point):Boolean = circle.center.distanceTo(point) <= circle.radius;
  
  def testIntersection(circle:Circle, line:Line):Boolean = {
    val p1c = circle.center - line.p1;
    val p1p2 = line.p2 - line.p1;
    val projectionVector = p1c.projection(p1p2);
    val nearestPoint = line.p1.displace(projectionVector.components);
    circle.intersects(nearestPoint);
  }
}