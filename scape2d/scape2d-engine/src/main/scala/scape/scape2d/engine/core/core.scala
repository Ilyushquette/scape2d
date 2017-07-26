package scape.scape2d.engine

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.elasticity._
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.getPositionAfter

package object core {
  private[core] def integrateMotion(movable:Movable[_], timestep:Double) = {
    val nextPosition = getPositionAfter(movable, timestep);
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
      val restoringForces = resolveRestoringForces(bond);
      particles._1.setForces(particles._1.forces :+ restoringForces._1);
      particles._2.setForces(particles._2.forces :+ restoringForces._2);
    }
  }
  
  private[core] def integrateOscillationsDamping(bond:Bond) = {
    val particles = bond.particles;
    val frictionalForces = resolveFrictionalForces(bond);
    particles._1.setForces(particles._1.forces :+ frictionalForces._1);
    particles._2.setForces(particles._2.forces :+ frictionalForces._2);
  }
}