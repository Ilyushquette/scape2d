package scape.scape2d.engine.motion

import java.lang.Math.abs
import java.lang.Math.signum

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.cos
import scape.scape2d.engine.geom.angle.sin
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.rotational.trajectory.trajectoryCircleOf
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.geom.shape.Shape

package object rotational {
  def angularDisplacementForTimeOf(movable:Movable[_ <: Shape]):(Duration => UnboundAngle) = {
    if(movable.isRotating) {
      val ω = movable.rotatable.get.angularVelocity;
      ω forTime _;
    }else _ => Angle.zero.unbound;
  }
  
  def positionForTimeOf(movable:Movable[_ <: Shape]):(Duration => Point) = {
    val p = movable.position;
    if(movable.isRotating) {
      val c = movable.rotatable.get.center;
      val r = c distanceTo p;
      val θ = c angleTo p;
      val ωt = angularDisplacementForTimeOf(movable);
      t => {
        val θt = θ + ωt(t).bound;
        val x = c.x + r * cos(θt);
        val y = c.y + r * sin(θt);
        Point(x, y);
      }
    }else _ => p;
  }
  
  def distanceForTimeOf(movable1:Movable[_ <: Shape], movable2:Movable[_ <: Shape]):(Duration => Double) = {
    val Pft = positionForTimeOf(movable1);
    val Qft = positionForTimeOf(movable2);
    t => Pft(t) distanceTo Qft(t);
  }
  
  def angularToLinearVelocity(movable:Movable[_ <: Shape]) = {
    val rotatable = movable.rotatable.get;
    val ωt = rotatable.angularVelocity.forTime(Second);
    val length = abs(trajectoryCircleOf(movable).forAngle(ωt));
    val radialDirection = rotatable.center angleTo movable.position;
    val θ = radialDirection + (Angle.right * signum(rotatable.angularVelocity.angle.value));
    Vector(length, θ) / Second;
  }
}