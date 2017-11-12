package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.rotate
import scape.scape2d.engine.core.matter.Particle

case class RotationIntegral {
  def integrate(particles:Iterable[Particle], timestep:Double) = {
    integrateRotation(particles, timestep);
  }
  
  private def integrateRotation(particles:Iterable[Particle], timestep:Double) = {
    particles.foreach(rotate(_, timestep));
    val bonds = particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
  }
}