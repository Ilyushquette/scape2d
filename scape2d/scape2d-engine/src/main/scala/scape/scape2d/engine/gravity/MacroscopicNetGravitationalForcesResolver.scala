package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.core.Matter

case class MacroscopicNetGravitationalForcesResolver(
  resolver:NetGravitationalForcesResolver,
  minimalAcceleration:InstantAcceleration
) extends NetGravitationalForcesResolver {
  def resolve[T <: Matter[_ <: FiniteShape]](matters:Set[T], timestep:Duration) = {
    val map = resolver.resolve(matters, timestep);
    map.filter(matterNetforce => isMacroscopic(matterNetforce._1, matterNetforce._2, timestep));
  }
  
  private def isMacroscopic[T <: Matter[_ <: FiniteShape]](matter:T, netforcePerTimestep:Vector, timestep:Duration) = {
    val accelerationVectorPerTimestep = matter.mass.forForce(netforcePerTimestep).velocity.vector;
    val minimalAccelerationVectorPerTimestep = minimalAcceleration.velocity.forTime(timestep);
    accelerationVectorPerTimestep >= minimalAccelerationVectorPerTimestep;
  }
}