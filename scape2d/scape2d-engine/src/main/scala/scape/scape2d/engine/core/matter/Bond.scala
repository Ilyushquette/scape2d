package scape.scape2d.engine.core.matter

import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.deformation.DeformationDescriptor
import scape.scape2d.engine.core.Volatile

class Bond private[matter] (
  val particles:Combination2[Particle, Particle],
  private var _restLength:Double,
  private var _deformationDescriptor:DeformationDescriptor,
  val dampingCoefficient:Double)
extends Volatile[Bond] {
  
  def reversed = new Bond(particles.reversed, _restLength, _deformationDescriptor, dampingCoefficient);
  
  def restLength = _restLength;
  
  private[core] def setRestLength(restLength:Double) = _restLength = restLength;
  
  def deformationDescriptor = _deformationDescriptor;
  
  private[core] def setDeformationDescriptor(deformationDescriptor:DeformationDescriptor) = {
    _deformationDescriptor = deformationDescriptor;
  }
  
  override def hashCode = particles.hashCode;
  
  override def equals(any:Any) = {
    if(any == null) false;
    else if(!any.isInstanceOf[Bond]) false;
    else{
      val other = any.asInstanceOf[Bond];
      particles == other.particles;
    }
  }
  
  def snapshot = snapshot();
  
  def snapshot(particles:Combination2[Particle, Particle] = particles,
               restLength:Double = _restLength,
               deformationDescriptor:DeformationDescriptor = _deformationDescriptor,
               dampingCoefficient:Double = dampingCoefficient) = {
    new Bond(particles, restLength, deformationDescriptor, dampingCoefficient);
  }
}