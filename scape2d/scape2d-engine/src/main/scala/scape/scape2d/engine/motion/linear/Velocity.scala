package scape.scape2d.engine.motion.linear

import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Second

object Velocity {
  val zero = Vector.zero / Second;
}

/**
 * distance in meters
 */
case class Velocity(vector:Vector, time:Duration) extends Ordered[Velocity] {
  lazy val vectorPerMillisecond = vector / time.milliseconds;
  
  def forDistance(distance:Double) = time * (distance / vector.magnitude);
  
  def forTime(time:Duration) = vector * (time / this.time);
  
  def +(velocity:Velocity) = {
    val vectorToAdd = velocity.forTime(time);
    Velocity(vector + vectorToAdd, time);
  }
  
  def -(velocity:Velocity) = {
    val vectorToSubtract = velocity.forTime(time);
    Velocity(vector - vectorToSubtract, time);
  }
  
  def *(multiplier:Double) = Velocity(vector * multiplier, time);
  
  def /(divider:Double) = Velocity(vector / divider, time);
  
  def compare(velocity:Velocity) = vectorPerMillisecond compareTo velocity.vectorPerMillisecond;
  
  override def equals(any:Any) = any match {
    case velocity:Velocity => vectorPerMillisecond == velocity.vectorPerMillisecond;
    case _ => false;
  }
}