package scape.scape2d.engine.motion.linear

import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.Sweepable
import scape.scape2d.engine.core.Identifiable

object LinearSweepFormingMovable {
  implicit def toShape(swept:LinearSweepFormingMovable[_]) = swept.shape;
  
  implicit def toEntity[T <: Movable with Formed[_ <: Sweepable[_]]  with Identifiable](swept:LinearSweepFormingMovable[T]) = {
    swept.entity;
  }
}

case class LinearSweepFormingMovable[T <: Movable with Formed[_ <: Sweepable[_]] with Identifiable](
  entity:T,
  timestep:Double = 0)
extends Formed[Shape] with Identifiable {
  val id = entity.id;
  
  lazy val shape = {
    val sweepVector = asMetersPerTimestep(entity.velocity, timestep);
    if(sweepVector.magnitude > 0) entity.shape.sweep(sweepVector).asInstanceOf[Shape];
    else entity.shape;
  }
}