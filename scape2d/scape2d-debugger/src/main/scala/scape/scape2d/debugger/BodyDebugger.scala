package scape.scape2d.debugger

import scape.scape2d.engine.core.matter.Body

class BodyDebugger(val particleDebugger:ParticleDebugger) {
  def trackBody(body:Body) = body.movables.foreach(particleDebugger.trackParticle);
}