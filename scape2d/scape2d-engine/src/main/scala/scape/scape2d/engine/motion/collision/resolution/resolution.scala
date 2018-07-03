package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.cos
import scape.scape2d.engine.geom.angle.sin
import scape.scape2d.engine.motion.linear.positionForTimeOf
import scape.scape2d.engine.util.LazyVal
import scape.scape2d.engine.time.Second

package object resolution {
  def resolveLinearVelocities(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val particle1 = snapshotPair._1;
    val particle2 = snapshotPair._2;
    val p1 = positionForTimeOf(particle1)(collision.time);
    val p2 = positionForTimeOf(particle2)(collision.time);
    val φ = p1 angleTo p2;
    
    def vel(particle1:Particle, particle2:Particle) = {
      val v1 = particle1.velocity.forTime(Second).magnitude;
      val v2 = particle2.velocity.forTime(Second).magnitude;
      val m1 = particle1.mass.kilograms;
      val m2 = particle2.mass.kilograms;
      val θ1 = particle1.velocity.vector.angle;
      val θ2 = particle2.velocity.vector.angle;
      
      val fraction = (v1 * cos(θ1 - φ) * (m1 - m2) + 2 * m2 * v2 * cos(θ2 - φ)) / (m1 + m2);
      val vx = fraction * cos(φ) + v1 * sin(θ1 - φ) * cos(φ + Angle.right);
      val vy = fraction * sin(φ) + v1 * sin(θ1 - φ) * sin(φ + Angle.right);
      Vector.from(Components(vx, vy));
    }
    
    (LazyVal(vel(particle1, particle2)), LazyVal(vel(particle2, particle1)));
  }
}