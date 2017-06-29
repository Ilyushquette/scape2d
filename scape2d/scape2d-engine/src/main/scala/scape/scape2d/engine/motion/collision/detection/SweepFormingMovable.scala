package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.motion.scaleVelocity

object SweepFormingMovable {
  implicit def toShape(swept:SweepFormingMovable[_]) = swept.shape;
  
  implicit def toEntity[T <: Movable[T] with Formed[_ <: Sweepable[_]]](swept:SweepFormingMovable[T]) = {
    swept.entity;
  }
}

case class SweepFormingMovable[T <: Movable[T] with Formed[_ <: Sweepable[_]]](
  entity:T,
  timestep:Double = 0)
extends Formed[Shape] {
  lazy val shape = {
    val sweepVector = scaleVelocity(entity.velocity, timestep);
    if(sweepVector.magnitude > 0) entity.shape.sweep(sweepVector).asInstanceOf[Shape];
    else entity.shape;
  }
}