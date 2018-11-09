package scape.scape2d.engine.gravity

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.when

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mock.MatterMock
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class MacroscopicNetGravitationalForcesResolverTest {
  @Test
  def testOnlyMacroscopicNetGravitationalForcesResolved = {
    val earth = new MatterMock(
        shape = Circle(Point.origin, 6371000),
        mass = Mass(5.972E24, Kilogram)
    );
    val ball = new MatterMock(
        shape = Circle(Point(0, 6371050), 1),
        mass = Mass(3, Kilogram)
    );
    val gravitationalForces = Map(earth -> Vector(29.4581337181, 90(Degree)),
                                  ball -> Vector(29.4581337181, 270(Degree)));
    val minimalAcceleration = InstantAcceleration(Vector(0.01, Angle.zero) / Second);
    val macroscopicGravitationalForces = Map(ball -> Vector(29.4581337181, 270(Degree)));
    
    val resolver = mock(classOf[NetGravitationalForcesResolver]);
    when(resolver.resolve(Set(earth, ball), Second)).thenReturn(gravitationalForces);
    val macroscopicResolver = MacroscopicNetGravitationalForcesResolver(resolver, minimalAcceleration);
    Assert.assertEquals(macroscopicGravitationalForces, macroscopicResolver.resolve(Set(earth, ball), Second));
  }
}