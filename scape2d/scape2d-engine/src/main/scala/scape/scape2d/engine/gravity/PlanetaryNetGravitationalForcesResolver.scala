package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.geom.shape.FiniteShape

case class PlanetaryNetGravitationalForcesResolver(
  planet:Matter[_ <: FiniteShape]
) extends NetGravitationalForcesResolver {
  def resolve[T <: Matter[_ <: FiniteShape]](matters:Set[T], timestep:Duration) = {
    val mapBuilder = Map.newBuilder[T, Vector];
    val timestepMultiplier = timestep / Second;
    for(matter <- matters)
      mapBuilder += (matter -> planet.gravitationalForceOnto(matter) * timestepMultiplier);
    mapBuilder.result();
  }
}