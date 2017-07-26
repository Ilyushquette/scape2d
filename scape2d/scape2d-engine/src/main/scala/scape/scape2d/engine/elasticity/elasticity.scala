package scape.scape2d.engine

import scape.scape2d.engine.core.matter.Bond

package object elasticity {
  def resolveFrictionalForces(bond:Bond) = {
    val relVelocity1 = bond.particles._1.velocity - bond.particles._2.velocity;
    val relVelocity2 = bond.particles._2.velocity - bond.particles._1.velocity;
    val frictionalForce1 = relVelocity1.opposite * bond.dampingCoefficient;
    val frictionalForce2 = relVelocity2.opposite * bond.dampingCoefficient;
    (frictionalForce1, frictionalForce2);
  }
  
  def resolveRestoringForces(bond:Bond) = {
    val particles = bond.particles;
    val currentLength = particles._1.position distanceTo particles._2.position;
    val strain = currentLength - bond.restLength;
    val stressMagnitude = bond.linearElastic.forStrain(strain);
    val restoringForce1 = (particles._2.position - particles._1.position) * stressMagnitude;
    val restoringForce2 = restoringForce1.opposite;
    (restoringForce1, restoringForce2);
  }
}