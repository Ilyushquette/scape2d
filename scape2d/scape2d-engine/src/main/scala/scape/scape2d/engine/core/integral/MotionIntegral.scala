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
import scape.scape2d.engine.motion.collision.CollisionEvent

case class MotionIntegral(
  collisionDetector:CollisionDetector[Particle] = BruteForceCollisionDetector(
      detectionStrategy = IterativeRootFindingCollisionDetectionStrategy()
  ),
  collisionForcesResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) {
  def integrate(particles:Set[Particle], timestep:Double):Unit = {
    particles.foreach(accelerate);
    val collisions = collisionDetector.detect(particles, timestep);
    integrate(particles, collisions, timestep);
  }
  
  private def integrate(particles:Set[Particle], collisions:Set[CollisionEvent[Particle]], timestep:Double):Unit = {
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val forces = collisionForcesResolver.resolve(earliestCollision);
      if(earliestCollision.time > 0) integrateMotion(particles, earliestCollision.time);
      exertKnockingForces(earliestCollision.concurrentPair, forces);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > 0) {
        val acceleratedParticles = particles.filter(accelerate);
        if(acceleratedParticles.size < 10) {
          val validCollisions = collisions.filterNot(containsAnyOfParticles(_, acceleratedParticles));
          val shiftedValidCollisions = validCollisions.map(shiftCollisionTime(_, earliestCollision.time));
          val revalidatedCollisions = collisionDetector.detect(acceleratedParticles, particles -- acceleratedParticles, remainingTime);
          integrate(particles, shiftedValidCollisions ++ revalidatedCollisions, remainingTime);
        }else integrate(particles, remainingTime);
      }
    }else integrateMotion(particles, timestep);
  }
  
  private def containsAnyOfParticles(collision:CollisionEvent[Particle], particles:Set[Particle]) = {
    particles.exists(collision.contains);
  }
  
  private def shiftCollisionTime(collision:CollisionEvent[Particle], timeshift:Double) = {
    collision.copy(time = collision.time - timeshift);
  }
  
  private def integrateMotion(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(move(_, timestep));
    val bonds = particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
  }
  
  private def exertKnockingForces(particles:(Particle, Particle), forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
}