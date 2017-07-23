package scape.scape2d.engine.core.matter

import java.lang.Math._
import scape.scape2d.engine.elasticity.LinearElastic
import scape.scape2d.engine.util.Combination2

object BondBuilder {
  def apply(p1:Particle, p2:Particle, youngModulus:Double) = {
    val radius = toRadians((p1.shape.radius + p2.shape.radius) / 2);
    // average cross-sectional area viewed along central axis
    val crossSectionalArea = PI * (radius * radius);
    val length = p1.position distanceTo p2.position;
    new BondBuilder(Combination2(p1, p2), length, LinearElastic(youngModulus, crossSectionalArea, length));
  }
  
  def apply(p1:Particle, p2:Particle) = {
    new BondBuilder(Combination2(p1, p2), p1.position distanceTo p2.position);
  }
}

case class BondBuilder private[matter] (
  particles:Combination2[Particle, Particle],
  restLength:Double,
  linearElastic:LinearElastic = LinearElastic(10),
  dampingCoefficient:Double = 0.1) {
  def withLengthAtRest(rl:Double) = copy(restLength = rl);
  
  def asLinearElastic(le:LinearElastic) = copy(linearElastic = le);
  
  def withDampingCoefficient(dc:Double) = copy(dampingCoefficient = dc);
  
  def build = Bond(particles, linearElastic, restLength, dampingCoefficient);
}