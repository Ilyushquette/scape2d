package scape.scape2d.debugger

import scape.scape2d.engine.core.matter.Bond

class BondDebugger(val particleDebugger:ParticleDebugger) {
  def trackBond(bond:Bond) = {
    particleDebugger.trackParticle(bond.particles._1);
    particleDebugger.trackParticle(bond.particles._2);
  }
}