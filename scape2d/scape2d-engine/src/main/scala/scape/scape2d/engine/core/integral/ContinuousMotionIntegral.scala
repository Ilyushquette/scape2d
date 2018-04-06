package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerate
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.move
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.resolution.ParticleCollisionForcesResolver
import scape.scape2d.engine.motion.collision.resolution.MomentumDeltaActionReactionalCollisionForcesResolver
import scape.scape2d.engine.motion.collision.detection.CollisionDetector
import scape.scape2d.engine.motion.collision.detection.IterativeRootFindingCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.BruteForceCollisionDetector
import scape.scape2d.engine.util.Combination2

case class ContinuousMotionIntegral(
  collisionDetector:CollisionDetector[Particle] = BruteForceCollisionDetector(
      detectionStrategy = IterativeRootFindingCollisionDetectionStrategy()
  ),
  collisionForcesResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) extends MotionIntegral {
  def integrate(particles:Set[Particle], timestep:Double):Unit = {
    particles.foreach(accelerate);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val forces = collisionForcesResolver.resolve(earliestCollision);
      if(earliestCollision.time > 0) integrateMotion(particles, earliestCollision.time);
      exertKnockingForces(earliestCollision.concurrentPair, forces);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > 0) integrate(particles, remainingTime);
    }else integrateMotion(particles, timestep);
  }
  
  private def integrateMotion(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(move(_, timestep));
    val bonds = particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
  }
  
  private def exertKnockingForces(particles:Combination2[Particle, Particle], forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
}