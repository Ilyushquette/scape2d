package scape.scape2d.engine.motion

import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.util.LazyVal
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.linear.getPostLinearMotionPosition
import scape.scape2d.engine.motion.linear.asMetersPerTimestep

package object collision {
  def findSafeTime[T <: Movable with Formed[_]](collision:CollisionEvent[T], closestDistance:Double) = {
    val snapshotPair = collision.snapshotPair;
    val faster = Seq(snapshotPair._1, snapshotPair._2).maxBy(_.velocity.magnitude);
    val scaledVelocity = asMetersPerTimestep(faster.velocity, collision.time);
    val distance = scaledVelocity.magnitude * collision.time;
    if(distance > closestDistance) {
      val safeDistance = distance - closestDistance;
      safeDistance / scaledVelocity.magnitude;
    }else 0;
  }
  
  def resolveVelocities(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val particle1 = snapshotPair._1;
    val particle2 = snapshotPair._2;
    val p1 = getPostLinearMotionPosition(particle1, collision.time);
    val p2 = getPostLinearMotionPosition(particle2, collision.time);
    val phi = p1.angleTo(p2);
    
    def vel(particle1:Particle, particle2:Particle) = {
      val u1 = particle1.velocity.magnitude;
      val u2 = particle2.velocity.magnitude;
      val m1 = particle1.mass;
      val m2 = particle2.mass;
      val a1 = particle1.velocity.angle;
      val a2 = particle2.velocity.angle;
      
      val fraction = (u1 * cosDeg(a1 - phi) * (m1 - m2) + 2 * m2 * u2 * cosDeg(a2 - phi)) / (m1 + m2);
      val vx = fraction * cosDeg(phi) + u1 * sinDeg(a1 - phi) * cosDeg(phi + 90);
      val vy = fraction * sinDeg(phi) + u1 * sinDeg(a1 - phi) * sinDeg(phi + 90);
      Vector.from(Components2D(vx, vy));
    }
    
    (LazyVal(vel(particle1, particle2)), LazyVal(vel(particle2, particle1)));
  }
  
  def resolveForces(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val velocities = resolveVelocities(collision);
    val particle1 = snapshotPair._1;
    val momentumBefore = particle1.velocity * particle1.mass;
    val momentumAfter = velocities._1 * particle1.mass;
    val force = momentumAfter - momentumBefore;
    (force, force.opposite);
  }
}