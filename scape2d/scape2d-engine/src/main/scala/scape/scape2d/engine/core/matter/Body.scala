package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.Rotatable

class Body private[matter] (
  val centerParticle:Particle,
  val bonds:Set[Bond],
  private var _angularVelocity:Double
) extends Rotatable {
  val movables = bonds.foldLeft(Set[Particle]())((particles, bond) => particles + bond.particles._1 + bond.particles._2);
  
  def center = centerParticle.position;
  
  def angularVelocity = _angularVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:Double) = _angularVelocity = newAngularVelocity;
  
  def snapshot = snapshot();
  
  def snapshot(centerParticle:Particle = this.centerParticle,
               bonds:Set[Bond] = this.bonds,
               angularVelocity:Double = this.angularVelocity) = {
    new Body(centerParticle, bonds, angularVelocity);
  }
}