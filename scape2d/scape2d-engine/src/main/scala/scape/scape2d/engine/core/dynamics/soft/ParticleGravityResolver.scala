package scape.scape2d.engine.core.dynamics.soft

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.gravity.NetGravitationalForcesResolver
import scape.scape2d.engine.gravity.UniversalNetGravitationalForcesResolver
import scape.scape2d.engine.time.Duration

case class ParticleGravityResolver(
  netGravitationalForcesResolver:NetGravitationalForcesResolver = UniversalNetGravitationalForcesResolver()
) {
  def resolve(particles:Set[Particle], timestep:Duration) = {
    val netGravitationalForces = netGravitationalForcesResolver.resolve(particles, timestep);
    netGravitationalForces.foreach {
      case (particle, force) => particle.exertForce(force, true);
    }
  }
}