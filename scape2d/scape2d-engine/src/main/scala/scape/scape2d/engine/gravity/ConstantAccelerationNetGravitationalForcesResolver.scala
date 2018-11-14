package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.motion.linear.Acceleration
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.FiniteShape

case class ConstantAccelerationNetGravitationalForcesResolver(
  acceleration:Acceleration
) extends NetGravitationalForcesResolver {
  def resolve[T <: Matter[_ <: FiniteShape]](matters:Set[T], timestep:Duration) = {
    val mapBuilder = Map.newBuilder[T, Vector];
    val velocityChangePerTimestep = acceleration.forTime(timestep);
    val instantAcceleration = InstantAcceleration(velocityChangePerTimestep);
    for(matter <- matters)
      mapBuilder += (matter -> matter.mass.forAcceleration(instantAcceleration));
    mapBuilder.result();
  }
}