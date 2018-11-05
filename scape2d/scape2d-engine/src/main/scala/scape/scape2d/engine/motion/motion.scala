package scape.scape2d.engine

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.linear.displacementForTimeOf
import scape.scape2d.engine.motion.rotational.{ positionForTimeOf => rotatedPositionForTimeOf }
import scape.scape2d.engine.motion.rotational.rotatedShapeForTimeOf
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

package object motion {
  def positionForTimeOf(movable:Movable[_ <: Shape]):(Duration => Point) = {
    val position = movable.position;
    val rotatedPositionForTime = rotatedPositionForTimeOf(movable);
    val linearDisplacementForTime = displacementForTimeOf(movable);
    t => if(t != Duration.zero) rotatedPositionForTime(t) + linearDisplacementForTime(t);
         else position;
  }
  
  def distanceForTimeOf(movable1:Movable[_ <: Shape], movable2:Movable[_ <: Shape]):(Duration => Double) = {
    val Pft = positionForTimeOf(movable1);
    val Qft = positionForTimeOf(movable2);
    t => Pft(t) distanceTo Qft(t);
  }
  
  def shapeForTimeOf[T <: Shape](movable:Movable[T]):(Duration => T) = {
    val rotatedShapeForTime = rotatedShapeForTimeOf(movable);
    val linearDisplacementForTime = displacementForTimeOf(movable);
    t => rotatedShapeForTime(t).displacedBy(linearDisplacementForTime(t)).asInstanceOf[T];
  }
}