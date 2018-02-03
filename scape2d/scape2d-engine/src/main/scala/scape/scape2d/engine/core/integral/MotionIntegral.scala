package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.matter.Particle

case class MotionIntegral(
  linearMotionIntegral:LinearMotionIntegral = LinearMotionIntegral(),
  rotationIntegral:RotationIntegral = RotationIntegral()
) {
  def integrate(particles:Iterable[Particle], timestep:Double):Unit = {
    val integratedLinearMotionTime = linearMotionIntegral.integrateBreakIfCollision(particles, timestep);
    val integratedRotationTime = rotationIntegral.integrateBreakIfCollision(particles, timestep);
    // Time for some type of integration might be skipped!!!
    // This issue will be investigated in motion mixing.
    val integratedTime = Math.max(integratedLinearMotionTime, integratedRotationTime);
    val remainingTime = timestep - integratedTime;
    if(remainingTime > 0) integrate(particles, timestep);
  }
}