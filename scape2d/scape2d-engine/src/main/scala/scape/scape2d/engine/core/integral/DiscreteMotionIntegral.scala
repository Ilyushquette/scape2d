package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerate
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.move
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.MovablePhantom
import scape.scape2d.engine.motion.collision.detection.PosterioriCollisionDetector
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.collision.resolution.ParticleCollisionForcesResolver
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.detection.BruteForcePosterioriCollisionDetector
import scape.scape2d.engine.motion.collision.resolution.MomentumDeltaActionReactionalCollisionForcesResolver
import scape.scape2d.engine.time.Duration

class DiscreteMotionIntegral(
  collisionDetector:PosterioriCollisionDetector[MovablePhantom[Particle]] = BruteForcePosterioriCollisionDetector(),
  collisionResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) extends MotionIntegral {
  def integrate(particles:Set[Particle], timestep:Duration) = {
    particles.foreach(accelerate);
    val particlePhantoms = particles.map(new MovablePhantom(_));
    particlePhantoms.foreach(move(_, timestep));
    val collisions = collisionDetector.detect(particlePhantoms);
    if(collisions.isEmpty) {
      particlePhantoms.foreach(_.commitPosition());
      val bonds = particles.flatMap(_.bonds);
      bonds.foreach(deform);
      bonds.foreach(dampOscillations);
    }else collisions.foreach(resolve);
  }
  
  private def resolve(phantomCollision:CollisionEvent[MovablePhantom[Particle]]) = {
    val collision = toParticleCollisionEvent(phantomCollision);
    val forces = collisionResolver.resolve(collision);
    exertKnockingForces(collision.concurrentPair, forces);
  }
  
  private def exertKnockingForces(particles:Combination2[Particle, Particle], forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
  
  private def toParticleCollisionEvent(phantomCollision:CollisionEvent[MovablePhantom[Particle]]) = {
    val particle1 = phantomCollision.concurrentPair._1.origin;
    val particle2 = phantomCollision.concurrentPair._2.origin;
    CollisionEvent(Combination2(particle1, particle2), phantomCollision.time);
  }
}