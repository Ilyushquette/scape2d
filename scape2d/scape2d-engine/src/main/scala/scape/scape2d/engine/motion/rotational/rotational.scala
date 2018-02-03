package scape.scape2d.engine.motion

import java.lang.Math.abs
import java.lang.Math.cos
import java.lang.Math.signum
import java.lang.Math.sin
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.normalizeDegrees
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

package object rotational {
  def positionForTimeOf(movable:Movable):(Double => Point) = {
    val Mp = movable.position;
    if(movable.rotatable.isDefined && movable.rotatable.get.angularVelocity != 0) {
      val Rc = movable.rotatable.get.center;
      val ω = movable.rotatable.get.angularVelocity;
      val r = Rc distanceTo Mp;
      val θ = Rc angleTo Mp;
      t => {
        val ωt = asRadiansPerTimestep(ω, t);
        val x = Rc.x + r * cos(θ + ωt);
        val y = Rc.y + r * sin(θ + ωt);
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
  
  def angularToLinearVelocity(movable:Movable) = {
    val rotatable = movable.rotatable.get;
    val radialDirection = rotatable.center angleToDeg movable.position;
    val ω = rotatable.angularVelocity;
    val r = movable.position distanceTo rotatable.center;
    val θ = normalizeDegrees(radialDirection + signum(rotatable.angularVelocity) * 90);
    Vector(abs(ω * r), θ);
  }
}