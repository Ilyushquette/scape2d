package scape.scape2d.engine.matter

import scape.scape2d.engine.geom.Point2D

class MotionEvent(val oldPosition:Point2D, val updatedParticle:Particle) extends Event {
  def triggerListeners = updatedParticle.motionListeners.foreach(_(this));
}