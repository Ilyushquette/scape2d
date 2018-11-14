package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.FiniteShape

case class IgnoredMatterNetGravitationalForcesResolver(
  resolver:NetGravitationalForcesResolver,
  ignoreList:List[_ <: Matter[_ <: FiniteShape]]
) extends NetGravitationalForcesResolver {
  def resolve[T <: Matter[_ <: FiniteShape]](matters:Set[T], timestep:Duration) = {
    val passedMatters = matters.filterNot(ignoreList.contains);
    resolver.resolve(passedMatters, timestep);
  }
}