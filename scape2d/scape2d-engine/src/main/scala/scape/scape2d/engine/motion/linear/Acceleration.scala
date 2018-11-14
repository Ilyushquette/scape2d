package scape.scape2d.engine.motion.linear

import scape.scape2d.engine.time.Duration

case class Acceleration(velocity:Velocity, time:Duration) {
  def forScalarVelocityChange(magnitude:Double, time:Duration) = {
    this.time * (magnitude / velocity.forTime(time).magnitude);
  }
  
  def forTime(time:Duration) = velocity * (time / this.time);
}