package scape.scape2d.debugger

import scape.scape2d.debugger.view.DebugView
import scape.scape2d.engine.matter.Particle

class Debugger(val view:DebugView) {
  def trackParticle(particle:Particle) = {
    view.renderParticle(particle);
    particle.addMotionListener(view.renderMotion(_, _));
  }
}