package scape.scape2d.debugger

import scape.scape2d.debugger.view.ParticleTrackingView
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.MotionEvent

class ParticleDebugger(val particleTrackingView:ParticleTrackingView) {
  def trackParticle(trackedParticle:MovableTrackerProxy[Particle]):Unit = {
    particleTrackingView.renderParticle(trackedParticle);
    trackedParticle.onMotion(renderMotion);
  }
  
  def trackParticle(particle:Particle):Unit = trackParticle(MovableTrackerProxy.track(particle));
  
  private def renderMotion(motion:MotionEvent[Particle]) = {
    particleTrackingView.clearParticle(motion.old);
    motion.old.bonds.foreach(particleTrackingView.clearBond);
    
    particleTrackingView.renderParticle(motion.snapshot);
    motion.snapshot.bonds.foreach(particleTrackingView.renderBond);
  }
}