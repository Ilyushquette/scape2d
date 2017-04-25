package scape.scape2d.debugger.view

import scape.scape2d.engine.matter.MotionEvent
import scape.scape2d.engine.matter.Particle

trait DebugView {
  def renderParticle(particle:Particle);
  def renderMotion(event:MotionEvent);
}