package scape.scape2d.engine

import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.deformation.elasticity.resolveFrictionalForces
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.{positionForTimeOf => postLinearMotionPosition}
import scape.scape2d.engine.motion.rotational.{positionForTimeOf => postRotationPosition}
import scape.scape2d.engine.core.matter.Body

package object core {
  private[core] def move(movable:Movable, timestep:Double) = {
    moveLinear(movable, timestep);
    rotate(movable, timestep);
  }
  
  private[core] def moveLinear(movable:Movable, timestep:Double) = {
    val nextPosition = postLinearMotionPosition(movable)(timestep);
    if(movable.position != nextPosition) movable.setPosition(nextPosition);
  }
  
  private[core] def rotate(movable:Movable, timestep:Double):Unit = {
    val nextPosition = postRotationPosition(movable)(timestep);
    if(movable.position != nextPosition) movable.setPosition(nextPosition);
  }
  
  private[core] def rotate(rotatable:Rotatable, timestep:Double):Unit = {
    rotatable.movables.foreach(rotate(_, timestep));
  }
  
  private[core] def accelerate(particle:Particle) = {
    accelerateLinear(particle);
    particle.rotatable.map(accelerateAngular);
  }
  
  /**
   * Since netforce in the particle is a representation of impulse J = N x timestep,
   * acceleration in meters per second per timestep too.
   * In the other words: all forces collected since last time integration
   * Final velocity of the particle in meters per second.
   */
  private[core] def accelerateLinear(particle:Particle) = {
    if(particle.force.magnitude > 0) {
      val acceleration = Vector(particle.force.magnitude / particle.mass, particle.force.angle);
      particle.setVelocity(particle.velocity + acceleration);
      particle.resetForce();
    }
  }
  
  /**
   * Since nettorque in the body is a representation of impulse J = N x timestep,
   * acceleration in radians per second per timestep too.
   * In the other words: all torques collected since last time integration.
   * Final angular velocity of the body in radians per second.
   */
  private[core] def accelerateAngular(body:Body) = {
    if(body.torque != 0) {
      val acceleration = body.torque / body.momentsOfInertia;
      body.setAngularVelocity(body.angularVelocity + acceleration);
      body.resetTorque();
    }
  }
  
  private[core] def deform(bond:Bond) = {
    val particles = bond.particles;
    val currentLength = particles._1.position distanceTo particles._2.position;
    val strain = currentLength - bond.restLength;
    if(!fuzzyEquals(0, strain, Epsilon)) {
      if(Math.abs(strain) <= bond.deformationDescriptor.plastic.limit) {
        val deformation = bond.deformationDescriptor.strained(strain);
        val restoringForce1 = (particles._1.position - particles._2.position) * -deformation.stress;
        val restoringForce2 = restoringForce1.opposite;
        val plasticStrain = bond.deformationDescriptor.plastic.limit - deformation.evolvedDescriptor.plastic.limit;
        particles._1.exertForce(restoringForce1, false);
        particles._2.exertForce(restoringForce2, false);
        bond.setRestLength(bond.restLength + plasticStrain);
        bond.setDeformationDescriptor(deformation.evolvedDescriptor);
        particles._2.setBonds(particles._2.bonds - bond + bond.reversed);
      }else bond.break();
    }
  }
  
  private[core] def dampOscillations(bond:Bond) = {
    val particles = bond.particles;
    val frictionalForces = resolveFrictionalForces(bond);
    particles._1.exertForce(frictionalForces._1, false);
    particles._2.exertForce(frictionalForces._2, false);
  }
}