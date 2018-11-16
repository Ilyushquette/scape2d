package scape.scape2d.engine.motion.collision.resolution

import java.lang.Math.min
import java.lang.Math.pow

import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.geom.shape.FiniteShape

case class RestitutionalActionReactionalCollisionForcesResolver(
  frictionForcesResolver:RigidBodyFrictionForcesResolver = CoulombModelRigidBodyFrictionForcesResolver()
) extends RigidBodyCollisionForcesResolver {
  def resolve(collision:RichCollisionEvent[RigidBody[_ <: FiniteShape]]) = {
    val collisionForcesResolutions = Map.newBuilder[Contact, (Vector, Vector)];
    for(contact <- collision.contactContainer.contacts) collisionForcesResolutions += contact -> resolveForContact(collision, contact);
    collisionForcesResolutions.result();
  }
  
  def resolveForContact(collision:RichCollisionEvent[RigidBody[_ <: FiniteShape]], contact:Contact) = {
    val rigidBody1 = collision.snapshotPair._1;
    val rigidBody2 = collision.snapshotPair._2;
    val p1 = collision.contactContainer.shape1.center;
    val p2 = collision.contactContainer.shape2.center;
    val combinedVelocityAtContact1 = rigidBody1.velocity + rigidBody1.angularVelocity.toLinearVelocity(p1, contact.point);
    val combinedVelocityAtContact2 = rigidBody2.velocity + rigidBody2.angularVelocity.toLinearVelocity(p2, contact.point);
    val combinedRelativeVelocityAtContact = combinedVelocityAtContact1 - combinedVelocityAtContact2;
    
    val vr = combinedRelativeVelocityAtContact.forTime(Second);
    val m1 = rigidBody1.mass.kilograms;
    val m2 = rigidBody2.mass.kilograms;
    val I1 = rigidBody1.momentOfInertia.value;
    val I2 = rigidBody2.momentOfInertia.value;
    val r1 = contact.point - p1;
    val r2 = contact.point - p2;
    val n = Vector.unit(contact.angle.opposite);
    val e = min(rigidBody1.restitutionCoefficient, rigidBody2.restitutionCoefficient);
    val force = resolveForce(vr, m1, m2, I1, I2, r1, r2, n, e);
    val forces = (force, force.opposite);
    val frictionForces = frictionForcesResolver.resolve(collision, contact, forces);
    (forces._1 + frictionForces._1, forces._2 + frictionForces._2);
  }
  
  private def resolveForce(vr:Vector, m1:Double, m2:Double, I1:Double, I2:Double, r1:Vector, r2:Vector, n:Vector, e:Double) = {
    val vrn = vr * n;
    if(vrn < 0) {
      val numerator = -(1 + e) * vrn;
      val denominator = n * n * ((1 / m1) + (1 / m2)) + pow(r1 x n, 2) / I1 + pow(r2 x n, 2) / I2;
      n * numerator / denominator;
    }else Vector.zero;
  }
}