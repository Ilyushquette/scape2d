package scape.scape2d.engine.motion.linear

import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.Vector

/**
 * distance in meters
 */
case class Velocity(vector:Vector, time:Duration) extends Ordered[Velocity] {
  lazy val vectorPerMillisecond = vector / time.milliseconds;
  
  def compare(velocity:Velocity) = vectorPerMillisecond compareTo velocity.vectorPerMillisecond;
  
  override def equals(any:Any) = any match {
    case velocity:Velocity => vectorPerMillisecond == velocity.vectorPerMillisecond;
    case _ => false;
  }
}