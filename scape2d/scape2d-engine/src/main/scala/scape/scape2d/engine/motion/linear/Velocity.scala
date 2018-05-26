package scape.scape2d.engine.motion.linear

import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.Vector

/**
 * distance in meters
 */
case class Velocity(vector:Vector, time:Duration) {
  lazy val vectorPerMillisecond = vector / time.milliseconds;
}