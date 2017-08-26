package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.MotionEvent
import scape.scape2d.engine.core.matter.Bond

trait ParticleTrackingView {
  def renderParticle(particle:Particle);
  
  def clearParticle(particle:Particle);
  
  def renderBond(bond:Bond);
  
  def clearBond(bond:Bond);
}