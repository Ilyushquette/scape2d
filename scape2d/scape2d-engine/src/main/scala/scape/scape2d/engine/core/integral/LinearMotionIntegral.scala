package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerate
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.moveLinear
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetector
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.resolution.ParticleCollisionForcesResolver
import scape.scape2d.engine.motion.collision.resolution.MomentumDeltaActionReactionalCollisionForcesResolver
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.BruteForceLinearMotionCollisionDetector
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.time.Duration

case class LinearMotionIntegral(
  collisionDetector:LinearMotionCollisionDetector[Particle] = BruteForceLinearMotionCollisionDetector(
      detectionStrategy = QuadraticLinearMotionCollisionDetectionStrategy()
  ),
  collisionForcesResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) {
  def integrate(particles:Set[Particle], timestep:Duration):Unit = {
    particles.foreach(accelerate);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val forces = collisionForcesResolver.resolve(earliestCollision);
      if(earliestCollision.time > Duration.zero) integrateLinearMotion(particles, earliestCollision.time);
      exertKnockingForces(earliestCollision.concurrentPair, forces);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > Duration.zero) integrate(particles, remainingTime);
    }else integrateLinearMotion(particles, timestep);
  }
  
  def integrateBreakIfCollision(particles:Set[Particle], timestep:Duration) = {
    particles.foreach(accelerate);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val forces = collisionForcesResolver.resolve(earliestCollision);
      if(earliestCollision.time > Duration.zero) integrateLinearMotion(particles, earliestCollision.time);
      exertKnockingForces(earliestCollision.concurrentPair, forces);
      earliestCollision.time;
    }else {
      integrateLinearMotion(particles, timestep);
      timestep;
    }
  }
  
  private def integrateLinearMotion(particles:Iterable[Particle], timestep:Duration) = {
    particles.foreach(moveLinear(_, timestep));
    val bonds = particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
  }
  
  private def exertKnockingForces(particles:Combination2[Particle, Particle], forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
}