package scape.scape2d.engine.motion.collision.detection

class CollisionDetectionException extends Exception;
case class NoDetectionException() extends CollisionDetectionException;
case class UnexpectedDetectionException() extends CollisionDetectionException;
case class ContactTimePredictionException() extends CollisionDetectionException;