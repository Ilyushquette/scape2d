package scape.scape2d.engine.core.dynamics.soft.linear

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.dynamics.soft.ParticleCollisionResolver
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.collision.detection.linear.BruteForceLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.time.Duration

case class ContinuousDetectionCollidingLinearSoftBodyDynamics(
  collisionDetector:LinearMotionCollisionDetector[Particle] = BruteForceLinearMotionCollisionDetector(
      detectionStrategy = QuadraticLinearMotionCollisionDetectionStrategy()
  ),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  linearSoftBodyDynamics:LinearSoftBodyDynamics = new LinearSoftBodyDynamics()
) extends Dynamics {
  def movables = linearSoftBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val collisions = collisionDetector.detect(linearSoftBodyDynamics.movables, timestep);
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
}