package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.motion.rotational.AngularVelocity

class Body private[matter] (
  val centerParticle:Particle,
  val bonds:Set[Bond],
  private var _angularVelocity:AngularVelocity,
  private var _torque:Double
) extends Rotatable {
  val movables = bonds.foldLeft(Set[Particle]())((particles, bond) => particles + bond.particles._1 + bond.particles._2);
  
  def center = centerParticle.position;
  
  def angularVelocity = _angularVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:AngularVelocity) = _angularVelocity = newAngularVelocity;
  
  def torque = _torque;
  
  private[core] def exertTorque(torque:Double) = _torque = _torque + torque;
  
  private[core] def resetTorque() = _torque = 0;
  
  def momentsOfInertia = movables.foldLeft(0d)(_ + _.momentOfInertia);
  
  def snapshot = snapshot();
  
  def snapshot(centerParticle:Particle = this.centerParticle,
               bonds:Set[Bond] = this.bonds,
               angularVelocity:AngularVelocity = this.angularVelocity,
               torque:Double = this.torque) = {
    new Body(centerParticle, bonds, angularVelocity, torque);
  }
}