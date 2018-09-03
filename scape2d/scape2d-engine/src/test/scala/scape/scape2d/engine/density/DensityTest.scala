package scape.scape2d.engine.density

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.AreaEpsilon
import scape.scape2d.engine.mass.Gram
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass

class DensityTest {
  @Test
  def testMassForAreaResolutionFromDensity = {
    val density = Density(60(Kilogram), 2);
    Assert.assertEquals(180(Kilogram), density forArea 6);
  }
  
  @Test
  def testAreaForMassResolutionFromDensity = {
    val density = Density(300(Gram), 5);
    Assert.assertEquals(2.5, density forMass 150(Gram), AreaEpsilon);
  }
}