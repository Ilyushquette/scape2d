package scape.scape2d.engine.core.matter

import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.deformation.DeformationDescriptor
import scape.scape2d.engine.core.Volatile
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.deformation.LinearStressStrainGraph

class Bond private[matter] (
  val particles:Combination2[Particle, Particle],
  private var _body:Option[Body],
  private var _restLength:Double,
  private var _deformationDescriptor:DeformationDescriptor,
  val dampingCoefficient:Double)
extends Volatile {
  // this package private default constructor is only exists for cglib proxy support
  private[matter] def this() = this(
      particles = Combination2(new Particle, new Particle), 
      _body = None,
      _restLength = 0,
      _deformationDescriptor = DeformationDescriptor(
          elastic = Elastic(LinearStressStrainGraph(1), 3), 
          plastic = Plastic(LinearStressStrainGraph(1), 5)), 
      dampingCoefficient = 0.1);
  
  def reversed = new Bond(particles.reversed, _body, _restLength, _deformationDescriptor, dampingCoefficient);
  
  def body = _body;
  
  private[core] def setBody(newBody:Option[Body]) = _body = newBody;
  
  def restLength = _restLength;
  
  private[core] def setRestLength(restLength:Double) = _restLength = restLength;
  
  def deformationDescriptor = _deformationDescriptor;
  
  private[core] def setDeformationDescriptor(deformationDescriptor:DeformationDescriptor) = {
    _deformationDescriptor = deformationDescriptor;
  }
  
  private[core] def break() = {
    if(connected) {
      particles._1.setBonds(particles._1.bonds - this);
      particles._2.setBonds(particles._2.bonds - this);
    }else throw new IllegalStateException("Already broken");
  }
  
  def connected = particles._1.bonds.contains(this) && particles._2.bonds.contains(this);
  
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
               body:Option[Body] = _body,
               restLength:Double = _restLength,
               deformationDescriptor:DeformationDescriptor = _deformationDescriptor,
               dampingCoefficient:Double = dampingCoefficient) = {
    new Bond(particles, body, restLength, deformationDescriptor, dampingCoefficient);
  }
}