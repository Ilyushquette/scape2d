package scape.scape2d.engine.motion.collision.resolution

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.cosDeg
import scape.scape2d.engine.geom.sinDeg
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.linear.getPostLinearMotionPosition
import scape.scape2d.engine.motion.rotational.angularToLinearVelocity

case class MomentumDeltaActionReactionalCollisionForcesResolver extends ParticleCollisionForcesResolver {
  def resolve(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val velocity1 = resolveCombinedVelocityOfFirstParticle(collision);
    val particle1 = snapshotPair._1;
    val momentumBefore = particle1.velocity * particle1.mass;
    val momentumAfter = velocity1 * particle1.mass;
    val force = momentumAfter - momentumBefore;
    (force, force.opposite);
  }
  
  private def resolveCombinedVelocityOfFirstParticle(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val particle1 = snapshotPair._1;
    val particle2 = snapshotPair._2;
    val combinedVelocity1 = combineAngularAndLinearVelocitiesOf(particle1);
    val combinedVelocity2 = combineAngularAndLinearVelocitiesOf(particle2);
    val p1 = getPostLinearMotionPosition(particle1, collision.time);
    val p2 = getPostLinearMotionPosition(particle2, collision.time);
    val φ = p1 angleToDeg p2;
    
    val v1 = combinedVelocity1.magnitude;
    val v2 = combinedVelocity2.magnitude;
    val m1 = particle1.mass;
    val m2 = particle2.mass;
    val θ1 = combinedVelocity1.angle;
    val θ2 = combinedVelocity2.angle;
    
    val fraction = (v1 * cosDeg(θ1 - φ) * (m1 - m2) + 2 * m2 * v2 * cosDeg(θ2 - φ)) / (m1 + m2);
    val vx = fraction * cosDeg(φ) + v1 * sinDeg(θ1 - φ) * cosDeg(φ + 90);
    val vy = fraction * sinDeg(φ) + v1 * sinDeg(θ1 - φ) * sinDeg(φ + 90);
    Vector.from(Components(vx, vy));
  }
  
  private def combineAngularAndLinearVelocitiesOf(particle:Particle) = {
    if(particle.rotatable.isDefined) particle.velocity + angularToLinearVelocity(particle);
    else particle.velocity;
  }
}