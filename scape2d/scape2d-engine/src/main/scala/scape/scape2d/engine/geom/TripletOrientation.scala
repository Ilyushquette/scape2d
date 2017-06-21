package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Point

sealed trait TripletOrientation;
case object Clockwise extends TripletOrientation;
case object CounterClockwise extends TripletOrientation;
case object Collinear extends TripletOrientation;

object TripletOrientation {
  def apply(p1:Point, p2:Point, p3:Point) = {
    val slopeDiff = (p2.y - p1.y) * (p3.x - p2.x) - (p3.y - p2.y) * (p2.x - p1.x);
    if(slopeDiff > Epsilon) Clockwise;
    else if(slopeDiff < -Epsilon) CounterClockwise;
    else Collinear;
  }
}