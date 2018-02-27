package scape.scape2d.engine.motion

import java.lang.Math.abs
import java.lang.Math.PI
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.ShapeBounds
import scape.scape2d.engine.geom.shape.CompositeShape
import scape.scape2d.engine.motion.rotational.trajectory.trajectoryOf

case class MotionBounds[T <: Movable with Formed[_ <: Shape]](
  movable:T,
  timestep:Double
) extends Formed[Shape] {
  val shape = {
    val ft = positionForTimeOf(movable);
    val displacementVector1 = ft(timestep / 2) - movable.position;
    val displacementVector2 = ft(timestep) - movable.position;
    val shape1 = movable.shape displacedBy displacementVector1;
    val shape2 = movable.shape displacedBy displacementVector2;
    ShapeBounds(CompositeShape(movable.shape, shape1, shape2));
  }
}