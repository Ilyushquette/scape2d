package scape.scape2d.engine.core.matter

import java.lang.Math.PI
import java.lang.Math.toRadians

import scape.scape2d.engine.deformation.DeformationDescriptor
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.util.Combination2

object BondBuilder {
  def apply(p1:Particle, p2:Particle, youngModulus:Double) = {
    val radius = toRadians((p1.shape.radius + p2.shape.radius) / 2);
    // average cross-sectional area viewed along central axis
    val crossSectionalArea = PI * (radius * radius);
    val length = p1.position distanceTo p2.position;
    val linearGraph = LinearStressStrainGraph(youngModulus, crossSectionalArea, length);
    val bondBuilder = new BondBuilder(Combination2(p1, p2), length);
    // retain default limits but replace graphs
    bondBuilder.asElastic(bondBuilder.elastic.copy(graph = linearGraph));
    bondBuilder.asPlastic(bondBuilder.plastic.copy(graph = linearGraph));
    bondBuilder;
  }
  
  def apply(p1:Particle, p2:Particle) = {
    new BondBuilder(Combination2(p1, p2), p1.position distanceTo p2.position);
  }
}

case class BondBuilder private[matter] (
  particles:Combination2[Particle, Particle],
  restLength:Double,
  elastic:Elastic = Elastic(LinearStressStrainGraph(1), 3),
  plastic:Plastic = Plastic(LinearStressStrainGraph(1), 5),
  dampingCoefficient:Double = 0.1) {
  
  def withLengthAtRest(rl:Double) = copy(restLength = rl);
  
  def asElastic(e:Elastic) = copy(elastic = e);
  
  def asPlastic(p:Plastic) = copy(plastic = p);
  
  def withDampingCoefficient(dc:Double) = copy(dampingCoefficient = dc);
  
  def build = new Bond(particles, None, restLength, DeformationDescriptor(elastic, plastic), dampingCoefficient);
}