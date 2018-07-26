package scape.scape2d.engine.core.dynamics.soft

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.collision.detection.BruteForceCollisionDetector
import scape.scape2d.engine.motion.collision.detection.CollisionDetector
import scape.scape2d.engine.motion.collision.detection.IterativeRootFindingCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration

case class ContinuousDetectionCollidingSoftBodyDynamics(
  collisionDetector:CollisionDetector[Particle] = BruteForceCollisionDetector(
      detectionStrategy = IterativeRootFindingCollisionDetectionStrategy()
  ),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  softBodyDynamics:SoftBodyDynamics = new SoftBodyDynamics()
) extends Dynamics {
  def movables = softBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val collisions = collisionDetector.detect(softBodyDynamics.movables, timestep);
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
}