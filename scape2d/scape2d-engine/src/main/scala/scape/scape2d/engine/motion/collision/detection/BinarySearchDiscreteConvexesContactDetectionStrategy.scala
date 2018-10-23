package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.shape.ConvexShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.ContactContainer
import scape.scape2d.engine.motion.shapeForTimeOf
import scape.scape2d.engine.time.Duration

case class BinarySearchDiscreteConvexesContactDetectionStrategy()
extends DiscreteContactDetectionStrategy[ConvexShape] {
  def detect(movable1:Movable[_ <: ConvexShape], movable2:Movable[_ <: ConvexShape], timestep:Duration) = {
    val shape1ForTime = shapeForTimeOf(movable1);
    val shape2ForTime = shapeForTimeOf(movable2);
    val shape1 = shape1ForTime(timestep);
    val shape2 = shape2ForTime(timestep);
    if(!shape2.contains(shape1.center)) {
      val intersectionPointSolutions = shape1.perimeter intersectionPointsWith shape2.perimeter;
      val intersectionPoints = intersectionPointSolutions.flatten.toList;
      if(intersectionPoints.size > 1) {
        val pointsCloserToShape1 = intersectionPoints.sortBy(_ distanceTo shape1.center);
        val contact = makeContact(shape1, pointsCloserToShape1(0), pointsCloserToShape1(1));
        Some(ContactContainer(shape1, shape2, List(contact), timestep));
      }else None;
    }else Some(binarySearchContact(shape1ForTime, shape2ForTime, Duration.zero, timestep));
  }
  
  private def binarySearchContact(shape1ForTime:Duration => ConvexShape,
                                  shape2ForTime:Duration => ConvexShape,
                                  offset:Duration,
                                  timestep:Duration):ContactContainer = {
    val halvedTimestep = timestep / 2;
    val targetTime = offset + halvedTimestep;
    val shape1 = shape1ForTime(targetTime);
    val shape2 = shape2ForTime(targetTime);
    if(!shape2.contains(shape1.center)) {
      val intersectionPointSolutions = shape1.perimeter intersectionPointsWith shape2.perimeter;
      val intersectionPoints = intersectionPointSolutions.flatten.toList;
      if(intersectionPoints.size > 1) {
        val pointsCloserToShape1 = intersectionPoints.sortBy(_ distanceTo shape1.center);
        val contact = makeContact(shape1, pointsCloserToShape1(0), pointsCloserToShape1(1));
        ContactContainer(shape1, shape2, List(contact), targetTime);
      }else binarySearchContact(shape1ForTime, shape2ForTime, targetTime, halvedTimestep);
    }else binarySearchContact(shape1ForTime, shape2ForTime, offset, halvedTimestep);
  }
  
  private def makeContact(shape1:ConvexShape, intersectionPoint1:Point, intersectionPoint2:Point) = {
    val contactSegment = Segment(intersectionPoint1, intersectionPoint2);
    val contactAngle = makeContactAngle(shape1, contactSegment);
    Contact(contactSegment.center, contactAngle);
  }
  
  private def makeContactAngle(shape1:ConvexShape, contactSegment:Segment) = {
    val normalAngle1 = contactSegment.line.angle + Angle.bound(90, Degree);
    val normalAngle2 = contactSegment.line.angle - Angle.bound(90, Degree);
    val angleToContact = shape1.center angleTo contactSegment.center;
    if(angleToContact.smallestDifferenceTo(normalAngle1) < angleToContact.smallestDifferenceTo(normalAngle2))
      normalAngle1;
    else
      normalAngle2;
  }
}