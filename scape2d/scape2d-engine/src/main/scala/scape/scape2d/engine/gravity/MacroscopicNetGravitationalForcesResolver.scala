package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second

case class MacroscopicNetGravitationalForcesResolver(
  resolver:NetGravitationalForcesResolver,
  minimalAcceleration:InstantAcceleration
) extends NetGravitationalForcesResolver {
  def resolve(particles:Set[Particle], timestep:Duration) = {
    val map = resolver.resolve(particles, timestep);
    map.filter(particleAndForce => isMacroscopic(particleAndForce._1, particleAndForce._2, timestep));
  }
  
  private def isMacroscopic(particle:Particle, forcePerTimestep:Vector, timestep:Duration) = {
    val accelerationVectorPerTimestep = particle.mass.forForce(forcePerTimestep).velocity.vector;
    val minimalAccelerationVectorPerTimestep = minimalAcceleration.velocity.forTime(timestep);
    accelerationVectorPerTimestep >= minimalAccelerationVectorPerTimestep;
  }
}