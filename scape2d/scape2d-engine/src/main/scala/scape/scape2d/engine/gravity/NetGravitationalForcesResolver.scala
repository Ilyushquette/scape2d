package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration

trait NetGravitationalForcesResolver {
  def resolve(particles:Set[Particle], timestep:Duration):Map[Particle, Vector];
}