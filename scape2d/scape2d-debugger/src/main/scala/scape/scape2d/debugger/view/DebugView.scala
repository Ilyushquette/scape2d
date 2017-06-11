package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.shape.Point

trait DebugView {
  def renderParticle(particle:Particle);
  def renderMotion(oldPosition:Point, particle:Particle);
}