package scape.scape2d.engine.core.dynamics.soft

import scape.scape2d.engine.core.accelerate
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.resolution.MomentumDeltaActionReactionalCollisionForcesResolver
import scape.scape2d.engine.motion.collision.resolution.ParticleCollisionForcesResolver
import scape.scape2d.engine.util.Combination2

case class ParticleCollisionResolver(
  collisionForcesResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) {
  def resolve(collision:CollisionEvent[Particle]) = {
    val knockingForces = collisionForcesResolver.resolve(collision);
    exertKnockingForces(collision.concurrentPair, knockingForces);
    accelerateParticles(collision.concurrentPair);
  }
  
  private def exertKnockingForces(particles:Combination2[Particle, Particle], forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
  
  private def accelerateParticles(particles:Combination2[Particle, Particle]) = {
    accelerate(particles._1);
    accelerate(particles._2);
  }
}