package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.accelerateAngular
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.rotate

case class RotationIntegral {
  def integrate(particles:Iterable[Particle], timestep:Double) = {
    integrateRotation(particles, timestep);
  }
  
  private def integrateRotation(particles:Iterable[Particle], timestep:Double) = {
    val bodies = particles.flatMap(_.rotatable).toSet;
    bodies.foreach(accelerateAngular);
    bodies.foreach(rotate(_, timestep));
  }
}