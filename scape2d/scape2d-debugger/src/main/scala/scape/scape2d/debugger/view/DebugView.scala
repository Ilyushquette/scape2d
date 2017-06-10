package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Point2D

trait DebugView {
  def renderParticle(particle:Particle);
  def renderMotion(oldPosition:Point2D, particle:Particle);
}