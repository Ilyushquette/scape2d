package scape.scape2d.engine.gravity

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.time.Second

class UniversalNetGravitationalForcesResolverTest {
  @Test
  def testUniversalGravitationNetForcesResolution = {
    val moon = ParticleBuilder()
               .as(Circle(Point.origin, 1737000))
               .withMass(Mass(7.348E22, Kilogram))
               .build;
    val particle = ParticleBuilder()
                   .as(Circle(Point(0, 1737050), 2))
                   .withMass(Mass(50, Kilogram))
                   .build;
    val resolver = UniversalNetGravitationalForcesResolver();
    val expectedForces = Map(moon -> Vector(81.2644714824, 90(Degree)),
                             particle -> Vector(81.2644714824, 270(Degree)));
    Assert.assertEquals(expectedForces, resolver.resolve(Set(moon, particle), Second));
  }
}