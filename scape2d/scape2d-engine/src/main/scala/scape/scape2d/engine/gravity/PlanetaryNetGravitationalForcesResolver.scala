package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration

case class PlanetaryNetGravitationalForcesResolver(
  planet:Particle
) extends NetGravitationalForcesResolver {
  def resolve(particles:Set[Particle], timestep:Duration) = {
    val mapBuilder = Map.newBuilder[Particle, Vector];
    for(particle <- particles) mapBuilder += (particle -> planet.gravitationalForceOnto(particle, timestep));
    mapBuilder.result();
  }
}