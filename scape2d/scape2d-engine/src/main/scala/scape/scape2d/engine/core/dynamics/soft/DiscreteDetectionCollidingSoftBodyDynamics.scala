package scape.scape2d.engine.core.dynamics.soft

import scala.annotation.migration

import scape.scape2d.engine.core.move
import scape.scape2d.engine.core.MovablePhantom
import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.detection.BruteForcePosterioriCollisionDetector
import scape.scape2d.engine.motion.collision.detection.PosterioriCollisionDetector
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2

case class DiscreteDetectionCollidingSoftBodyDynamics(
  collisionDetector:PosterioriCollisionDetector[MovablePhantom[Particle]] = BruteForcePosterioriCollisionDetector(),
  collisionResolver:ParticleCollisionResolver = ParticleCollisionResolver(),
  softBodyDynamics:SoftBodyDynamics = new SoftBodyDynamics()
) extends Dynamics {
  def movables = softBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val phantomMovables = softBodyDynamics.movables.map(new MovablePhantom(_));
    phantomMovables.foreach(move(_, timestep));
    val collisions = collisionDetector.detect(phantomMovables);
    if(!collisions.isEmpty) {
      collisions.foreach(toParticleCollisionEvent _ andThen collisionResolver.resolve);
      integrate(timestep);
    }else softBodyDynamics.integrate(timestep);
  }
  
  private def toParticleCollisionEvent(phantomCollision:CollisionEvent[MovablePhantom[Particle]]) = {
    val particle1 = phantomCollision.concurrentPair._1.origin;
    val particle2 = phantomCollision.concurrentPair._2.origin;
    CollisionEvent(Combination2(particle1, particle2), phantomCollision.time);
  }
}