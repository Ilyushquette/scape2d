package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.MotionEvent

trait ParticleTrackingView {
  def renderParticle(particle:Particle);
  def renderMotion(motion:MotionEvent[Particle]);
}