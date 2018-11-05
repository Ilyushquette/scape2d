package scape.scape2d.engine.core.dynamics.soft.linear

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.dynamics.soft.ParticleCollisionResolver
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.detection.broad.BruteForceContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.ContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2

case class ContinuousDetectionCollidingLinearSoftBodyDynamics(
  broadPhaseDetectionStrategy:ContinuousBroadPhaseCollisionDetectionStrategy[Particle] =
    BruteForceContinuousBroadPhaseCollisionDetectionStrategy(),
  detectionStrategy:LinearMotionCollisionDetectionStrategy[Particle] =
    QuadraticLinearMotionCollisionDetectionStrategy(),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  linearSoftBodyDynamics:LinearSoftBodyDynamics = new LinearSoftBodyDynamics()
) extends Dynamics {
  def movables = linearSoftBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val suspectedCollidingParticles = broadPhaseDetectionStrategy.prune(movables, timestep);
    val collisions = suspectedCollidingParticles.flatMap(detectCollision(_, timestep));
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      if(earliestCollision.time > Duration.zero)
        linearSoftBodyDynamics.integrate(earliestCollision.time);
      collisionResolver.resolve(earliestCollision);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > Duration.zero)
        integrate(remainingTime);
    }else linearSoftBodyDynamics.integrate(timestep);
  }
  
  private def detectCollision(particles:Combination2[Particle, Particle], timestep:Duration) = {
    val detection = detectionStrategy.detect(particles._1, particles._2, timestep);
    detection.map(CollisionEvent(particles, _));
  }
}