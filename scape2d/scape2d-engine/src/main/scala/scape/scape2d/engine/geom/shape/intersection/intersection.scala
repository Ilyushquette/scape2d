package scape.scape2d.engine.geom.shape

import java.lang.Math._
import scape.scape2d.engine.geom._
import com.google.common.math.DoubleMath._

package object intersection {
  def testIntersection(p1:Point, p2:Point) = p1 == p2;
  
  def testIntersection(line:Line, point:Point):Boolean = {
    if(line.vertical) fuzzyEquals(point.x, line.forY(point.y), Epsilon);
    else fuzzyEquals(point.y, line.forX(point.x), Epsilon);
  }
  
  def testIntersection(l1:Line, l2:Line):Boolean = {
    if(!l1.vertical && !l2.vertical) 
      !fuzzyEquals(l1.slope.get, l2.slope.get, Epsilon) ||
      fuzzyEquals(l1.yIntercept.get, l2.yIntercept.get, Epsilon);
    else if(l1.vertical && l2.vertical) fuzzyEquals(l1.p1.x, l2.p1.x, Epsilon);
    else true;
  }
  
  def testIntersection(ray:Ray, point:Point):Boolean = {
    ray.origin == point || fuzzyEquals(ray.angle, ray.origin.angleTo(point), Epsilon);
  }
  
  def testIntersection(ray:Ray, line:Line):Boolean = {
    if(ray.line.vertical && line.vertical) fuzzyEquals(ray.origin.x, line.p1.x, Epsilon);
    else if(!line.vertical && !ray.line.vertical && fuzzyEquals(line.slope.get, ray.line.slope.get, Epsilon))
      fuzzyEquals(ray.line.yIntercept.get, line.yIntercept.get, Epsilon);
    else {
      val mutualX = findMutualX(ray.line, line);
      if(90 < ray.angle && ray.angle < 270) mutualX <= ray.origin.x;
      else mutualX >= ray.origin.x;
    }
  }
  
  def testIntersection(r1:Ray, r2:Ray):Boolean = {
    val parallel = (r1.line.vertical && r2.line.vertical) ||
                   (!r1.line.vertical && !r2.line.vertical &&
                    fuzzyEquals(r1.line.slope.get, r2.line.slope.get, Epsilon));
    if(parallel) r1.intersects(r2.origin) || r2.intersects(r1.origin);
    else r1.intersects(r2.line) && r2.intersects(r1.line);
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
  
  def testIntersection(segment:Segment, ray:Ray):Boolean = {
    val r1r2s1orientation = TripletOrientation(ray.line.p1, ray.line.p2, segment.p1);
    val r1r2s2orientation = TripletOrientation(ray.line.p1, ray.line.p2, segment.p2);
    if(r1r2s1orientation == Collinear && r1r2s1orientation == r1r2s2orientation)
      ray.intersects(segment.p1) || ray.intersects(segment.p2);
    else if(r1r2s1orientation != r1r2s2orientation) {
      val mutualX = findMutualX(ray.line, segment.line);
      if(90 < ray.angle && ray.angle < 270) mutualX <= ray.origin.x;
      else mutualX >= ray.origin.x;
    }else false;
  }
  
  def testIntersection(circle:Circle, point:Point):Boolean = circle.center.distanceTo(point) <= circle.radius;
  
  def testIntersection(circle:Circle, line:Line):Boolean = {
    val p1c = circle.center - line.p1;
    val p1p2 = line.p2 - line.p1;
    val projectionVector = p1c.projection(p1p2);
    val nearestPoint = line.p1.displace(projectionVector.components);
    circle.intersects(nearestPoint);
  }
  
  def testIntersection(circle:Circle, ray:Ray):Boolean = {
    if(!circle.intersects(ray.origin)) {
      val sc = circle.center - ray.origin;
      val rayUnitVector = new Vector2D(1, ray.angle);
      val projectionVector = sc.projection(rayUnitVector);
      if(fuzzyEquals(projectionVector.angle, ray.angle, Epsilon)) {
        val nearestPoint = ray.origin.displace(projectionVector.components);
        circle.intersects(nearestPoint);
      }else false;
    }else true;
  }
  
  def testIntersection(circle:Circle, segment:Segment):Boolean = {
    if(!circle.intersects(segment.p1) && !circle.intersects(segment.p2)) {
      val p1c = circle.center - segment.p1;
      val p1p2 = segment.p2 - segment.p1;
      val projectionVector = p1c.projection(p1p2);
      if(fuzzyEquals(projectionVector.angle, p1p2.angle, Epsilon) &&
         projectionVector.magnitude < p1p2.magnitude) {
        val nearestPoint = segment.p1.displace(projectionVector.components);
        circle.intersects(nearestPoint);
      }else false;
    }else true;
  }
  
  def testIntersection(c1:Circle, c2:Circle):Boolean = {
    c1.center.distanceTo(c2.center) <= c1.radius + c2.radius;
  }
  
  def testIntersection(polygon:Polygon, point:Point):Boolean = {
    val onTheVertex = polygon.segments.exists(s => s.p1 == point || s.p2 == point);
    if(!onTheVertex) {
      val ray = Ray(point, 180);
      val intersectedSegments = polygon.segments.filter(_.intersects(ray));
      (intersectedSegments.size & 1) == 1;
    }else true;
  }
}