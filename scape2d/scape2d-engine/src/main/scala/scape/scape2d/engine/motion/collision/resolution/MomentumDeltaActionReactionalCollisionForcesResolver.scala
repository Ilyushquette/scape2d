package scape.scape2d.engine.motion.collision.resolution

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.cos
import scape.scape2d.engine.geom.angle.sin
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.motion.positionForTimeOf
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.motion.linear.Velocity

case class MomentumDeltaActionReactionalCollisionForcesResolver() extends ParticleCollisionForcesResolver {
  def resolve(collision:CollisionEvent[Particle]) = {
    val particle1 = collision.snapshotPair._1;
    
    val particle1VelocityBefore = combineAngularAndLinearVelocitiesOf(particle1);
    val particle1VelocityAfter = resolveCombinedVelocityOfFirstParticle(collision);
    
    val momentumBefore = particle1VelocityBefore.vector * particle1.mass.kilograms;
    val momentumAfter = particle1VelocityAfter.vector * particle1.mass.kilograms;
    
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
    
    val v1 = combinedVelocity1.forTime(Second).magnitude;
    val v2 = combinedVelocity2.forTime(Second).magnitude;
    val m1 = particle1.mass.kilograms;
    val m2 = particle2.mass.kilograms;
    val θ1 = combinedVelocity1.vector.angle;
    val θ2 = combinedVelocity2.vector.angle;
    
    val fraction = (v1 * cos(θ1 - φ) * (m1 - m2) + 2 * m2 * v2 * cos(θ2 - φ)) / (m1 + m2);
    val vx = fraction * cos(φ) + v1 * sin(θ1 - φ) * cos(φ + Angle.right);
    val vy = fraction * sin(φ) + v1 * sin(θ1 - φ) * sin(φ + Angle.right);
    Vector.from(Components(vx, vy)) / Second;
  }
  
  private def combineAngularAndLinearVelocitiesOf(particle:Particle) = {
    val rotatable = particle.rotatable;
    if(rotatable.isDefined)
      particle.velocity + rotatable.get.angularVelocity.toLinearVelocity(rotatable.get.center, particle.position);
    else particle.velocity;
  }
}