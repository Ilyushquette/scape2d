package scape.scape2d.engine.core.dynamics.soft

import java.util.HashMap
import java.util.Map

import scala.annotation.migration

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.move
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.detection.broad.BruteForceDiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.DiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2

case class DiscreteDetectionCollidingSoftBodyDynamics(
  broadPhaseCollisionDetectionStrategy:DiscreteBroadPhaseCollisionDetectionStrategy[Particle] =
    BruteForceDiscreteBroadPhaseCollisionDetectionStrategy(),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  softBodyDynamics:SoftBodyDynamics = new SoftBodyDynamics()
) extends Dynamics {
  def movables = softBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val particles = new HashMap[Int, Particle]();
    movables.foreach(particle => particles.put(particle.id, particle));
    val phantomParticles = movables.map(moveAsPhantom(_, timestep));
    val suspectedCollidingPhantomParticles = broadPhaseCollisionDetectionStrategy.prune(phantomParticles);
    val collisions = suspectedCollidingPhantomParticles.flatMap(detectCollision(particles, _, timestep));
    if(!collisions.isEmpty) {
      collisions.foreach(collisionResolver.resolve);
      integrate(timestep);
    }else softBodyDynamics.integrate(timestep);
  }
  
  private def moveAsPhantom(particle:Particle, timestep:Duration) = {
    val phantomParticle = particle.snapshotExcludingBonds;
    move(phantomParticle, timestep);
    phantomParticle;
  }
  
  private def detectCollision(particles:Map[Int, Particle], phantomParticles:Combination2[Particle, Particle], timestep:Duration) = {
    if(phantomParticles._1.shape intersects phantomParticles._2.shape) {
      val particle1 = particles.get(phantomParticles._1.id);
      val particle2 = particles.get(phantomParticles._2.id);
      Some(CollisionEvent(particle1, particle2, timestep));
    }else None;
  }
}