package scape.scape2d.engine.motion.collision.resolution

import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.core.matter.Particle

case class MomentumDeltaActionReactionalCollisionForcesResolver extends ParticleCollisionForcesResolver {
  def resolve(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val velocities = resolveVelocities(collision);
    val particle1 = snapshotPair._1;
    val momentumBefore = particle1.velocity * particle1.mass;
    val momentumAfter = velocities._1 * particle1.mass;
    val force = momentumAfter - momentumBefore;
    (force, force.opposite);
  }
}