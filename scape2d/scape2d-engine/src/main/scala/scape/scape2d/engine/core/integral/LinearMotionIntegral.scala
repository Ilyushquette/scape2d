package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerateLinear
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.moveLinear
import scape.scape2d.engine.motion.collision.detection.CollisionDetector
import scape.scape2d.engine.motion.collision.findSafeTime
import scape.scape2d.engine.motion.collision.resolveForces

case class LinearMotionIntegral(collisionDetector:CollisionDetector[Particle]) {
  def integrate(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(accelerateLinear);
    val collisions = collisionDetector.detect(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      val safeTime = findSafeTime(earliestCollision, 0.005);
      val forces = resolveForces(earliestCollision);
      if(safeTime > 0) integrateLinearMotion(particles, safeTime);
      earliestCollision.concurrentPair._1.setForces(earliestCollision.concurrentPair._1.forces :+ forces._1);
      earliestCollision.concurrentPair._2.setForces(earliestCollision.concurrentPair._2.forces :+ forces._2);
      val remainingTime = timestep - safeTime;
      if(remainingTime > 0) integrateLinearMotion(particles, remainingTime);
    }else integrateLinearMotion(particles, timestep);
  }
  
  private def integrateLinearMotion(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(moveLinear(_, timestep));
    val bonds = particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
  }
}