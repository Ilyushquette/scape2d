package scape.scape2d.debugger

import scape.scape2d.debugger.view.ParticleTrackingView
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.MovableTrackerProxy

class ParticleDebugger(val particleTrackingView:ParticleTrackingView) {
  def trackParticle(trackedParticle:MovableTrackerProxy[Particle]) = {
    particleTrackingView.renderParticle(trackedParticle);
    trackedParticle.addMotionListener(particleTrackingView.renderMotion);
  }
}