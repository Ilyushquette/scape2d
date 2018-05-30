package scape.scape2d.engine.motion

import java.lang.Math.abs
import java.lang.Math.signum
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.angle.cos
import scape.scape2d.engine.geom.angle.sin
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.motion.rotational.trajectory.trajectoryCircleOf
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second

package object rotational {
  def positionForTimeOf(movable:Movable):(Duration => Point) = {
    val Mp = movable.position;
    if(movable.isRotating) {
      val Rc = movable.rotatable.get.center;
      val ω = movable.rotatable.get.angularVelocity;
      val r = Rc distanceTo Mp;
      val θ = Rc angleTo Mp;
      t => {
        val ωt = ω forTime t;
        val θt = θ + ωt.bound;
        val x = Rc.x + r * cos(θt);
        val y = Rc.y + r * sin(θt);
        Point(x, y);
      }
    }else _ => Mp;
  }
  
  def distanceForTimeOf(movable1:Movable, movable2:Movable):(Duration => Double) = {
    val Pft = positionForTimeOf(movable1);
    val Qft = positionForTimeOf(movable2);
    t => Pft(t) distanceTo Qft(t);
  }
  
  def angularToLinearVelocity(movable:Movable) = {
    val rotatable = movable.rotatable.get;
    val ωt = rotatable.angularVelocity.forTime(Second);
    val length = abs(trajectoryCircleOf(movable).forAngle(ωt));
    val radialDirection = rotatable.center angleTo movable.position;
    val θ = radialDirection + (Angle.right * signum(rotatable.angularVelocity.angle.value));
    Vector(length, θ) / Second;
  }
}