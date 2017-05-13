package scape.scape2d.engine.motion.collision

class CollisionDetectionException(reason:String) extends Exception(reason);
case class NoDetectionException(reason:String) extends CollisionDetectionException(reason);
case class UnexpectedDetectionException(reason:String) extends CollisionDetectionException(reason);
case class ContactTimePredictionException(reason:String) extends CollisionDetectionException(reason);