package scape.scape2d.engine.motion

import java.lang.Math.toDegrees

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.normalizeAngle
import scape.scape2d.engine.geom.Vector2D

package object rotational {
  def getPostRotationPosition(movable:Movable, timestep:Double) = {
    if(movable.rotatable.isDefined && movable.rotatable.get.angularVelocity != 0) {
      val radiansPerTimestep = asRadiansPerTimestep(movable.rotatable.get.angularVelocity, timestep);
      val degreesPerTimestep = normalizeAngle(toDegrees(radiansPerTimestep));
      val radialVector = movable.position - movable.rotatable.get.center;
      val nextRadialVector = new Vector2D(radialVector.magnitude, radialVector.angle + degreesPerTimestep);
      movable.rotatable.get.center.displace(nextRadialVector.components);
    }else movable.position;
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