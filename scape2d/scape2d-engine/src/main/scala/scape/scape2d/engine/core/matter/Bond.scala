package scape.scape2d.engine.core.matter

import scape.scape2d.engine.elasticity.LinearElastic
import scape.scape2d.engine.util.Combination2

case class Bond private[matter] (
  particles:Combination2[Particle, Particle],
  linearElastic:LinearElastic,
  restLength:Double,
  dampingCoefficient:Double) {
  lazy val reversed = copy(particles = particles.reversed);
  
  override def hashCode = particles.hashCode;
  
  override def equals(any:Any) = any match {
    case Bond(oparticles, _, _, _) => particles == oparticles;
    case _ => false;
  }
}