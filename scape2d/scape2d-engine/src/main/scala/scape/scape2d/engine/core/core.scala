package scape.scape2d.engine

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.deformation.elasticity.resolveFrictionalForces
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.linear.getPostLinearMotionPosition

package object core {
  private[core] def integrateMotion(movable:Movable, timestep:Double) = {
    val nextPosition = getPostLinearMotionPosition(movable, timestep);
    if(movable.position != nextPosition) movable.setPosition(nextPosition);
  }
  
  /**
   * Since each force in the particle is a representation of impulse J = N x timestep,
   * acceleration in meters per second per timestep too.
   * In the other words: all forces collected since last time integration
   * Final velocity of the particle in meters per second.
   */
  private[core] def integrateAcceleration(particle:Particle) = {
    val forces = particle.forces;
    if(!forces.isEmpty) {
      val netforce = forces.reduce(_ + _);
      val acceleration = new Vector2D(netforce.magnitude / particle.mass, netforce.angle);
      particle.setVelocity(particle.velocity + acceleration);
      particle.setForces(Array.empty);
    }
  }
  
  private[core] def integrateDeformation(bond:Bond) = {
    val particles = bond.particles;
    val currentLength = particles._1.position distanceTo particles._2.position;
    val strain = currentLength - bond.restLength;
    if(!fuzzyEquals(0, strain, Epsilon)) {
      if(Math.abs(strain) <= bond.deformationDescriptor.plastic.limit) {
        val deformation = bond.deformationDescriptor.strained(strain);
        val restoringForce1 = (particles._1.position - particles._2.position) * -deformation.stress;
        val restoringForce2 = restoringForce1.opposite;
        val plasticStrain = bond.deformationDescriptor.plastic.limit - deformation.evolvedDescriptor.plastic.limit;
        particles._1.setForces(particles._1.forces :+ restoringForce1);
        particles._2.setForces(particles._2.forces :+ restoringForce2);
        bond.setRestLength(bond.restLength + plasticStrain);
        bond.setDeformationDescriptor(deformation.evolvedDescriptor);
        particles._2.setBonds(particles._2.bonds - bond + bond.reversed);
      }else bond.break();
    }
  }
  
  private[core] def integrateOscillationsDamping(bond:Bond) = {
    val particles = bond.particles;
    val frictionalForces = resolveFrictionalForces(bond);
    particles._1.setForces(particles._1.forces :+ frictionalForces._1);
    particles._2.setForces(particles._2.forces :+ frictionalForces._2);
  }
}