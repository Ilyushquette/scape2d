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
    val orientation1 = TripletOrientation(s1.p1, s1.p2, s2.p1);
    val orientation2 = TripletOrientation(s1.p1, s1.p2, s2.p2);
    val orientation3 = TripletOrientation(s2.p1, s2.p2, s1.p1);
    val orientation4 = TripletOrientation(s2.p1, s2.p2, s1.p2);
    if(orientation1 == Collinear && orientation1 == orientation2) { // all orientations are collinear then
      max(s1.p1.x, s1.p2.x) >= min(s2.p1.x, s2.p2.x) && min(s1.p1.x, s1.p2.x) <= max(s2.p1.x, s2.p2.x) &&
      max(s1.p1.y, s1.p2.y) >= min(s2.p1.y, s2.p2.y) && min(s1.p1.y, s1.p2.y) <= max(s2.p1.y, s2.p2.y);
    }else orientation1 != orientation2 && orientation3 != orientation4;
  }
  
  def testIntersection(segment:Segment, line:Line):Boolean = {
    val l1l2s1orientation = TripletOrientation(line.p1, line.p2, segment.p1);
    val l1l2s2orientation = TripletOrientation(line.p1, line.p2, segment.p2);
    l1l2s1orientation != l1l2s2orientation || l1l2s1orientation == Collinear;
  }
  
  def testIntersection(circle:Circle, point:Point):Boolean = circle.center.distanceTo(point) <= circle.radius;
  
  def testIntersection(circle:Circle, line:Line):Boolean = {
    val p1c = circle.center - line.p1;
    val p1p2 = line.p2 - line.p1;
    val projectionVector = p1c.projection(p1p2);
    val nearestPoint = line.p1.displace(projectionVector.components);
    circle.intersects(nearestPoint);
  }
  
  def testIntersection(circle:Circle, segment:Segment):Boolean = {
    if(!circle.intersects(segment.p1) && !circle.intersects(segment.p2)) {
      val p1c = circle.center - segment.p1;
      val p1p2 = segment.p2 - segment.p1;
      val projectionVector = p1c.projection(p1p2);
      if(projectionVector.angle == p1p2.angle && projectionVector.magnitude < p1p2.magnitude) {
        val nearestPoint = segment.p1.displace(projectionVector.components);
        circle.intersects(nearestPoint);
      }else false;
    }else true;
  }
  
  def testIntersection(c1:Circle, c2:Circle):Boolean = {
    c1.center.distanceTo(c2.center) <= c1.radius + c2.radius;
  }
}