package scape.scape2d.engine.core.integral

import scala.Option.option2Iterable
import scape.scape2d.engine.core.accelerateAngular
import scape.scape2d.engine.core.accelerateLinear
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.rotate
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetector
import scape.scape2d.engine.motion.collision.resolution.ParticleCollisionForcesResolver
import scape.scape2d.engine.motion.collision.resolution.MomentumDeltaActionReactionalCollisionForcesResolver
import scape.scape2d.engine.geom.Vector

case class RotationIntegral(
  collisionDetector:RotationalCollisionDetector[Particle],
  collisionForcesResolver:ParticleCollisionForcesResolver = MomentumDeltaActionReactionalCollisionForcesResolver()
) {
  def integrate(particles:Iterable[Particle], timestep:Double):Unit = {
    accelerate(particles);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val forces = collisionForcesResolver.resolve(earliestCollision);
      if(earliestCollision.time > 0) integrateRotation(particles, earliestCollision.time);
      exertKnockingForces(earliestCollision.concurrentPair, forces);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > 0) integrate(particles, remainingTime);
    }else integrateRotation(particles, timestep);
  }
  
  private def accelerate(particles:Iterable[Particle]) = {
    particles.foreach(accelerateLinear);
    val bodies = particles.flatMap(_.rotatable).toSet;
    bodies.foreach(accelerateAngular);
  }
  
  private def integrateRotation(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(rotate(_, timestep));
  }
  
  private def exertKnockingForces(particles:(Particle, Particle), forces:(Vector, Vector)) = {
    particles._1.exertForce(forces._1, true);
    particles._2.exertForce(forces._2, true);
  }
}