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

package object rotational {
  def positionForTimeOf(movable:Movable):(Double => Point) = {
    val Mp = movable.position;
    if(movable.isRotating) {
      val Rc = movable.rotatable.get.center;
      val ω = movable.rotatable.get.angularVelocity;
      val r = Rc distanceTo Mp;
      val θ = Rc angleTo Mp;
      t => {
        val ωt = asRadiansPerTimestep(ω, t);
        val θt = θ + Angle.bound(ωt, Radian);
        val x = Rc.x + r * cos(θt);
        val y = Rc.y + r * sin(θt);
        Point(x, y);
      }
    }else _ => Mp;
  }
  
  def distanceForTimeOf(movable1:Movable, movable2:Movable):(Double => Double) = {
    val Pft = positionForTimeOf(movable1);
    val Qft = positionForTimeOf(movable2);
    t => Pft(t) distanceTo Qft(t);
  }
  
  def asRadiansPerTimestep(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity / stepsPerSecond;
  }
  
  def asRadiansPerSecond(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity * stepsPerSecond;
  }
  
  def angularToLinearVelocityScalar(movable:Movable) = {
    val rotatable = movable.rotatable.get;
    val ω = rotatable.angularVelocity;
    val r = movable.position distanceTo rotatable.center;
    abs(ω * r);
  }
  
  def angularToLinearVelocity(movable:Movable) = {
    val scalar = angularToLinearVelocityScalar(movable);
    val rotatable = movable.rotatable.get;
    val radialDirection = rotatable.center angleTo movable.position;
    val θ = radialDirection + (Angle.right * signum(rotatable.angularVelocity));
    Vector(scalar, θ);
  }
}