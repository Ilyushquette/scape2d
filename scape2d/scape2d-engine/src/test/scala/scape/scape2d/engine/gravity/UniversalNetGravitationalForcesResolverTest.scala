package scape.scape2d.engine.gravity

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mock.MatterMock
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class UniversalNetGravitationalForcesResolverTest {
  @Test
  def testUniversalGravitationNetForcesResolution = {
    val moon = new MatterMock(
        shape = Circle(Point.origin, 1737000),
        mass = Mass(7.348E22, Kilogram)
    );
    val ball = new MatterMock(
        shape = Circle(Point(0, 1737050), 2),
        mass = Mass(50, Kilogram)
    );
    val resolver = UniversalNetGravitationalForcesResolver();
    val expectedForces = Map(moon -> Vector(81.2644714824, 90(Degree)),
                             ball -> Vector(81.2644714824, 270(Degree)));
    Assert.assertEquals(expectedForces, resolver.resolve(Set(moon, ball), Second));
  }
}