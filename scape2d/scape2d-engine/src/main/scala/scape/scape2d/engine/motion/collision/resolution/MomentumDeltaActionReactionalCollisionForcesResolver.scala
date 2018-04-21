package scape.scape2d.engine.motion.collision.resolution

import java.lang.Math._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.HalfPI
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.positionForTimeOf
import scape.scape2d.engine.motion.rotational.angularToLinearVelocity

case class MomentumDeltaActionReactionalCollisionForcesResolver() extends ParticleCollisionForcesResolver {
  def resolve(collision:CollisionEvent[Particle]) = {
    val snapshotPair = collision.snapshotPair;
    val velocity1 = resolveCombinedVelocityOfFirstParticle(collision);
    val particle1 = snapshotPair._1;
    val momentumBefore = combineAngularAndLinearVelocitiesOf(particle1) * particle1.mass;
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
    val p1 = positionForTimeOf(particle1)(collision.time);
    val p2 = positionForTimeOf(particle2)(collision.time);
    val φ = p1 angleTo p2;
    
    val v1 = combinedVelocity1.magnitude;
    val v2 = combinedVelocity2.magnitude;
    val m1 = particle1.mass;
    val m2 = particle2.mass;
    val θ1 = combinedVelocity1.angle;
    val θ2 = combinedVelocity2.angle;
    
    val fraction = (v1 * cos(θ1 - φ) * (m1 - m2) + 2 * m2 * v2 * cos(θ2 - φ)) / (m1 + m2);
    val vx = fraction * cos(φ) + v1 * sin(θ1 - φ) * cos(φ + HalfPI);
    val vy = fraction * sin(φ) + v1 * sin(θ1 - φ) * sin(φ + HalfPI);
    Vector.from(Components(vx, vy));
  }
  
  private def combineAngularAndLinearVelocitiesOf(particle:Particle) = {
    if(particle.rotatable.isDefined) particle.velocity + angularToLinearVelocity(particle);
    else particle.velocity;
  }
}