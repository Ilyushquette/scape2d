package scape.scape2d.engine.motion.collision.resolution

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

case class CoulombModelRigidBodyFrictionForcesResolver()
extends RigidBodyFrictionForcesResolver {
  def resolve(collision:RichCollisionEvent[RigidBody[_ <: FiniteShape]], contact:Contact, forces:(Vector, Vector)) = {
    val zeroForces = forces._1 == Vector.zero;
    if(!zeroForces) {
      val rigidBody1 = collision.snapshotPair._1;
      val rigidBody2 = collision.snapshotPair._2;
      val shape1 = collision.contactContainer.shape1;
      val shape2 = collision.contactContainer.shape2;
      val velocityAtContactAfterCollision1 = velocityAtContactAfterCollision(rigidBody1, shape1, contact, forces._1);
      val velocityAtContactAfterCollision2 = velocityAtContactAfterCollision(rigidBody2, shape2, contact, forces._2);
      val v1 = velocityAtContactAfterCollision1 forTime Second;
      val v2 = velocityAtContactAfterCollision2 forTime Second;
      val vr = v1 - v2;
      val m1 = rigidBody1.mass.kilograms;
      val m2 = rigidBody2.mass.kilograms;
      val n = Vector.unit(contact.angle.opposite);
      val µs = (rigidBody1.staticFrictionCoefficient + rigidBody2.staticFrictionCoefficient) / 2;
      val µk = (rigidBody2.kineticFrictionCoefficient + rigidBody2.kineticFrictionCoefficient) / 2;
      val frictionForce1 = resolveFrictionForce(vr, v1, m1, n, forces._1.magnitude, µs, µk);
      val frictionForce2 = resolveFrictionForce(vr.opposite, v2, m2, n.opposite, forces._2.magnitude, µs, µk);
      (frictionForce1, frictionForce2);
    }else forces;
  }
  
  private def resolveFrictionForce(vr:Vector, v:Vector, m:Double, n:Vector, fn:Double, µs:Double, µk:Double) = {
    val t = (v - n * (v * n)).toUnit;
    val vt = v * t;
    val ff = t * -(m * vt);
    if(ff.magnitude > fn * µs)
      t * -(fn * µk);
    else ff;
  }
  
  private def velocityAtContact(rigidBody:RigidBody[_ <: FiniteShape], shapeAtContact:FiniteShape, contact:Contact) = {
    rigidBody.velocity + rigidBody.angularVelocity.toLinearVelocity(shapeAtContact.center, contact.point);
  }
  
  private def accelerationAtContact(rigidBody:RigidBody[_ <: FiniteShape], shapeAtContact:FiniteShape, contact:Contact, force:Vector) = {
    val acceleration = rigidBody.accelerationForForce(force, contact.point);
    val angularAcceleration = rigidBody.angularAccelerationForForce(force, contact.point);
    acceleration.velocity + angularAcceleration.angularVelocity.toLinearVelocity(shapeAtContact.center, contact.point);
  }
  
  private def velocityAtContactAfterCollision(rigidBody:RigidBody[_ <: FiniteShape], shapeAtContact:FiniteShape, contact:Contact, force:Vector) = {
    val combinedVelocityAtContact = velocityAtContact(rigidBody, shapeAtContact, contact);
    combinedVelocityAtContact + accelerationAtContact(rigidBody, shapeAtContact, contact, force);
  }
}