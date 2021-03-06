package scape.scape2d.engine.deformation

import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.time.Second

package object elasticity {
  def resolveFrictionalForces(bond:Bond) = {
    val relVelocity1 = bond.particles._1.velocity - bond.particles._2.velocity;
    val relVelocity2 = bond.particles._2.velocity - bond.particles._1.velocity;
    val frictionalForce1 = relVelocity1.forTime(Second).opposite * bond.dampingCoefficient;
    val frictionalForce2 = relVelocity2.forTime(Second).opposite * bond.dampingCoefficient;
    (frictionalForce1, frictionalForce2);
  }
}