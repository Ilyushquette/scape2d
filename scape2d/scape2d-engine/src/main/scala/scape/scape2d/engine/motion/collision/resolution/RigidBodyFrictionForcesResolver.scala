package scape.scape2d.engine.motion.collision.resolution

import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.Contact
import scape.scape2d.engine.motion.collision.RichCollisionEvent
import scape.scape2d.engine.geom.shape.FiniteShape

trait RigidBodyFrictionForcesResolver {
  def resolve(collision:RichCollisionEvent[RigidBody[_ <: FiniteShape]], contact:Contact, forces:(Vector, Vector)):(Vector, Vector);
}