package scape.scape2d.engine.core.dynamics.soft

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.detection.CollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.IterativeRootFindingCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.BruteForceContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.ContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2

case class ContinuousDetectionCollidingSoftBodyDynamics(
  broadPhaseDetectionStrategy:ContinuousBroadPhaseCollisionDetectionStrategy[Particle] =
    BruteForceContinuousBroadPhaseCollisionDetectionStrategy(),
  detectionStrategy:CollisionDetectionStrategy[Particle] =
    IterativeRootFindingCollisionDetectionStrategy(),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  softBodyDynamics:SoftBodyDynamics = new SoftBodyDynamics()
) extends Dynamics {
  def movables = softBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val suspectedCollidingParticles = broadPhaseDetectionStrategy.prune(movables, timestep);
    val collisions = suspectedCollidingParticles.flatMap(detectCollision(_, timestep));
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      if(earliestCollision.time > Duration.zero)
        softBodyDynamics.integrate(earliestCollision.time);
      collisionResolver.resolve(earliestCollision);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > Duration.zero)
        integrate(remainingTime);
    }else softBodyDynamics.integrate(timestep);
  }
  
  private def detectCollision(particles:Combination2[Particle, Particle], timestep:Duration) = {
    val detection = detectionStrategy.detect(particles._1, particles._2, timestep);
    detection.map(CollisionEvent(particles, _));
  }
}