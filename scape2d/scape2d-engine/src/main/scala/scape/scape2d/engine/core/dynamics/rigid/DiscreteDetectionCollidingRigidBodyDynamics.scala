package scape.scape2d.engine.core.dynamics.rigid

import java.util.HashMap

import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.core.move
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.motion.collision.detection.DiscreteContactDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.DiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.resolution.RigidBodyCollisionForcesResolver
import scape.scape2d.engine.time.Duration

case class DiscreteDetectionCollidingRigidBodyDynamics[T >: Null <: FiniteShape](
  broadPhaseCollisionDetectionStrategy:DiscreteBroadPhaseCollisionDetectionStrategy[RigidBody[_ <: T]],
  contactDetectionStrategy:DiscreteContactDetectionStrategy[T],
  collisionForcesResolver:RigidBodyCollisionForcesResolver,
  rigidBodyDynamics:RigidBodyDynamics[T] = new RigidBodyDynamics()
) extends Dynamics {
  def movables = rigidBodyDynamics.movables;
  
  def integrate(timestep:Duration) = {
    val collisions = detectCollisions(timestep);
    if(!collisions.isEmpty) {
      val resolvedCollisions = collisions.count(resolveCollision);
      if(resolvedCollisions > 0) integrate(timestep);
      else rigidBodyDynamics.integrate(timestep);
    }else rigidBodyDynamics.integrate(timestep);
  }
  
  private def detectCollisions(timestep:Duration):Set[RichCollisionEvent[RigidBody[_ <: T]]] = {
    val rigidBodies = new HashMap[Int, RigidBody[_ <: T]]();
    movables.foreach(rigidBody => rigidBodies.put(rigidBody.id, rigidBody));
    val phantomRigidBodies = movables.map(moveAsPhantom(_, timestep));
    val suspectedCollidingPhantomRigidBodies = broadPhaseCollisionDetectionStrategy.prune(phantomRigidBodies);
    suspectedCollidingPhantomRigidBodies.flatMap(phantomCombo => {
      val id1 = phantomCombo._1.id;
      val id2 = phantomCombo._2.id;
      val contactDetection = contactDetectionStrategy.detect(phantomCombo._1, phantomCombo._2, timestep);
      contactDetection.map(RichCollisionEvent(rigidBodies.get(id1), rigidBodies.get(id2), _));
    });
  }
  
  private def moveAsPhantom(rigidBody:RigidBody[_ <: T], timestep:Duration) = {
    val phantomBody = rigidBody.snapshot;
    move(phantomBody, timestep);
    phantomBody;
  }
  
  private def resolveCollision(collision:RichCollisionEvent[RigidBody[_ <: T]]) = {
    val contactForcesResolutions = collisionForcesResolver.resolve(collision);
    val resolvedContacts = contactForcesResolutions.count(r => resolveContact(collision, r._1, r._2));
    resolvedContacts > 0;
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