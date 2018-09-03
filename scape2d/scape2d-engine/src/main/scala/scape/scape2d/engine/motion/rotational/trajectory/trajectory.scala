package scape.scape2d.engine.motion.rotational

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed.exposeShape
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Shape

package object trajectory {
  def trajectoryCircleOf(movable:Movable[_ <: Shape]) = {
    val trajectoryCenter = movable.rotatable.get.center;
    val trajectoryRadius = trajectoryCenter distanceTo movable.position;
    Circle(trajectoryCenter, trajectoryRadius);
  }
  
  def trajectoryOf[T <: Movable[Circle]](movable:T) = {
    Ring(trajectoryCircleOf(movable), movable.radius * 2);
  }
  
  def pathOf[T <: Movable[Circle]](movable:T) = {
    if(movable.isRotating)
      trajectoryOf(movable);
    else movable.shape;
  }
  
  def crossPaths[T <: Movable[Circle]](movable1:T, movable2:T) = {
    val path1 = pathOf(movable1);
    val path2 = pathOf(movable2);
    path1.intersects(path2);
  }
}