package scape.scape2d.debugger

import scape.scape2d.debugger.view.DebugView
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.MovableTrackerProxy

class Debugger(val view:DebugView) {
  def trackParticle(trackedParticle:MovableTrackerProxy[Particle]) = {
    view.renderParticle(trackedParticle);
    trackedParticle.addMotionListener(view.renderMotion);
  }
}