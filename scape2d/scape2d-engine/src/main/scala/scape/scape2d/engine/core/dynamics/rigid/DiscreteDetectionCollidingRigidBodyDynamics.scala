package scape.scape2d.engine.core.dynamics.rigid

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.motion.collision.detection.PosterioriRichCollisionDetector
import scape.scape2d.engine.motion.collision.resolution.RigidBodyCollisionForcesResolver
import scape.scape2d.engine.time.Duration

case class DiscreteDetectionCollidingRigidBodyDynamics[T >: Null <: FiniteShape](
  collisionDetector:PosterioriRichCollisionDetector[T],
  collisionForcesResolver:RigidBodyCollisionForcesResolver,
  rigidBodyDynamics:RigidBodyDynamics[T] = new RigidBodyDynamics()
) extends Dynamics {
  def movables = rigidBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val collisions = collisionDetector.detect(movables, timestep);
    if(!collisions.isEmpty) {
      val configurationBalanced = collisions.exists(resolveCollision);
      if(configurationBalanced) integrate(timestep);
      else rigidBodyDynamics.integrate(timestep);
    }else rigidBodyDynamics.integrate(timestep);
  }
  
  private def resolveCollision(collision:RichCollisionEvent[RigidBody[_ <: T]]) = {
    val contactForcesResolutions = collisionForcesResolver.resolve(collision);
    contactForcesResolutions.exists(r => resolveContact(collision, r._1, r._2));
  }
  
  private def resolveContact(collision:RichCollisionEvent[RigidBody[_ <: T]], contact:Contact, forces:(Vector, Vector)) = {
    val zeroForces = forces._1 == Vector.zero;
    if(!zeroForces) {
      collision.concurrentPair._1.exertForce(forces._1, contact.point);
      collision.concurrentPair._2.exertForce(forces._2, contact.point);
      true;
    }else false;
  }
}