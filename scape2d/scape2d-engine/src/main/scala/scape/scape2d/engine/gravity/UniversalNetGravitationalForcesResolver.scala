package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration

case class UniversalNetGravitationalForcesResolver() extends NetGravitationalForcesResolver {
  def resolve(particles:Set[Particle], timestep:Duration) = {
    val mapBuilder = Map.newBuilder[Particle, Vector];
    for(particle <- particles) mapBuilder += (particle -> resolve(particle, particles, timestep));
    mapBuilder.result();
  }
  
  def resolve(particle:Particle, particles:Set[Particle], timestep:Duration) = {
    particles.foldLeft(Vector.zero)(_ + _.gravitationalForceOnto(particle, timestep));
  }
}