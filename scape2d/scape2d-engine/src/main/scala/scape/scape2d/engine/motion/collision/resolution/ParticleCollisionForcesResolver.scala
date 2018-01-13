package scape.scape2d.engine.motion.collision.resolution

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.collision.CollisionEvent

trait ParticleCollisionForcesResolver {
  def resolve(collision:CollisionEvent[Particle]):(Vector, Vector);
}