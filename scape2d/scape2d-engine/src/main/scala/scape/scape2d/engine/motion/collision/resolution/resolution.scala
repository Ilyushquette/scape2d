package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.cosDeg
import scape.scape2d.engine.geom.sinDeg
import scape.scape2d.engine.motion.linear.getPostLinearMotionPosition
import scape.scape2d.engine.util.LazyVal

package object resolution {
  def resolveVelocities(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val particle1 = snapshotPair._1;
    val particle2 = snapshotPair._2;
    val p1 = getPostLinearMotionPosition(particle1, collision.time);
    val p2 = getPostLinearMotionPosition(particle2, collision.time);
    val φ = p1 angleToDeg p2;
    
    def vel(particle1:Particle, particle2:Particle) = {
      val v1 = particle1.velocity.magnitude;
      val v2 = particle2.velocity.magnitude;
      val m1 = particle1.mass;
      val m2 = particle2.mass;
      val θ1 = particle1.velocity.angle;
      val θ2 = particle2.velocity.angle;
      
      val fraction = (v1 * cosDeg(θ1 - φ) * (m1 - m2) + 2 * m2 * v2 * cosDeg(θ2 - φ)) / (m1 + m2);
      val vx = fraction * cosDeg(φ) + v1 * sinDeg(θ1 - φ) * cosDeg(φ + 90);
      val vy = fraction * sinDeg(φ) + v1 * sinDeg(θ1 - φ) * sinDeg(φ + 90);
      Vector.from(Components(vx, vy));
    }
    
    (LazyVal(vel(particle1, particle2)), LazyVal(vel(particle2, particle1)));
  }
}