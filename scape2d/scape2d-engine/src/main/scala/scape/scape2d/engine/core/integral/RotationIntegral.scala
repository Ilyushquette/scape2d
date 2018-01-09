package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerateAngular
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.rotate
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetector

case class RotationIntegral(collisionDetector:RotationalCollisionDetector[Particle]) {
  def integrate(particles:Iterable[Particle], timestep:Double):Unit = {
    val bodies = particles.flatMap(_.rotatable).toSet;
    bodies.foreach(accelerateAngular);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      if(earliestCollision.time > 0) integrateRotation(particles, earliestCollision.time);
      // collision response is out of the scope of this story
      // for now simple reset of angular velocities
      stop(earliestCollision.concurrentPair);
      val remainingTime = timestep - earliestCollision.time;
      if(remainingTime > 0) integrate(particles, remainingTime);
    }else integrateRotation(particles, timestep);
  }
  
  private def integrateRotation(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(rotate(_, timestep));
  }
  
  private def stop(particles:(Particle, Particle)) = {
    particles._1.rotatable.map(_.setAngularVelocity(0));
    particles._2.rotatable.map(_.setAngularVelocity(0));
  }
}