package scape.scape2d.engine.motion.rotational

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Ring

package object trajectory {
  def trajectoryOf[T <: Movable with Formed[Circle]](movable:T) = {
    val trajectoryCenter = movable.rotatable.get.center;
    val trajectoryRadius = trajectoryCenter distanceTo movable.position;
    Ring(Circle(trajectoryCenter, trajectoryRadius), movable.radius * 2);
  }
}