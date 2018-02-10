package scape.scape2d.engine

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.linear.{positionForTimeOf => postLinearMotionPosition}
import scape.scape2d.engine.motion.rotational.{positionForTimeOf => postRotationPosition}

package object motion {
  def positionForTimeOf(movable:Movable):(Double => Point) = {
    val position = movable.position;
    val ft_linear = postLinearMotionPosition(movable);
    val ft_rotational = postRotationPosition(movable);
    t => {
      val p1 = ft_linear(t);
      val p2 = ft_rotational(t);
      val angularDisplacementVector = p2 - position;
      p1 + angularDisplacementVector;
    }
  }
  
  def distanceForTimeOf(movable1:Movable, movable2:Movable):(Double => Double) = {
    val Pft = positionForTimeOf(movable1);
    val Qft = positionForTimeOf(movable2);
    t => Pft(t) distanceTo Qft(t);
  }
}