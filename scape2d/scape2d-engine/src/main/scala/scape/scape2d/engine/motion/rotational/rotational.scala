package scape.scape2d.engine.motion

import java.lang.Math.cos
import java.lang.Math.sin

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point

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
  
  def asRadiansPerTimestep(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity / stepsPerSecond;
  }
  
  def asRadiansPerSecond(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity * stepsPerSecond;
  }
}