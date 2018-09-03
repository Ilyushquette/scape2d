package scape.scape2d.engine.density

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass

class DensityTest {
  @Test
  def testMassForAreaResolutionFromDensity = {
    val density = Density(60(Kilogram), 2);
    Assert.assertEquals(180(Kilogram), density forArea 6);
  }
}