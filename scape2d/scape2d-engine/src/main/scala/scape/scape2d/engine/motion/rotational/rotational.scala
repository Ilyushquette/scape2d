package scape.scape2d.engine.motion

package object rotational {
  def asRadiansPerTimestep(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity / stepsPerSecond;
  }
  
  def asRadiansPerSecond(angularVelocity:Double, timestep:Double) = {
    val stepsPerSecond = 1000 / timestep;
    angularVelocity * stepsPerSecond;
  }
}