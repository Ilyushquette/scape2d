package scape.scape2d.engine.gravity

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mock.MatterMock
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class ConstantAccelerationNetGravitationalForcesResolverTest {
  @Test
  def testMatterUnderConstantAccelerationNetGravitationalForcesResolution = {
    val box = new MatterMock(
        shape = AxisAlignedRectangle(Point.origin, 5, 5),
        mass = Mass(25, Kilogram)
    );
    val ball = new MatterMock(
        shape = Circle(Point(20, 20), 2),
        mass = Mass(10, Kilogram)
    );
    val resolver = ConstantAccelerationNetGravitationalForcesResolver(Vector(9.8, 270(Degree)) / Second / Second);
    val expectedForces = Map(box -> Vector(245, 270(Degree)),
                             ball -> Vector(98, 270(Degree)));
    Assert.assertEquals(expectedForces, resolver.resolve(Set(box, ball), Second));
  }
}