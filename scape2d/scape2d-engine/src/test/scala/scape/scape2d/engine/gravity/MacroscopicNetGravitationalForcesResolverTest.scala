package scape.scape2d.engine.gravity

import org.junit.Test
import org.mockito.Mockito._

import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.motion.linear.InstantAcceleration
import org.junit.Assert

class MacroscopicNetGravitationalForcesResolverTest {
  @Test
  def testOnlyMacroscopicNetGravitationalForcesResolved = {
    val earth = ParticleBuilder()
                .as(Circle(Point.origin, 6371000))
                .withMass(Mass(5.972E24, Kilogram))
                .build;
    val particle = ParticleBuilder()
                   .as(Circle(Point(0, 6371050), 1))
                   .withMass(Mass(3, Kilogram))
                   .build;
    val gravitationalForces = Map(earth -> Vector(29.4581337181, 90(Degree)),
                                  particle -> Vector(29.4581337181, 270(Degree)));
    val minimalAcceleration = InstantAcceleration(Vector(0.01, Angle.zero) / Second);
    val macroscopicGravitationalForces = Map(particle -> Vector(29.4581337181, 270(Degree)));
    
    val resolver = mock(classOf[NetGravitationalForcesResolver]);
    when(resolver.resolve(Set(earth, particle), Second)).thenReturn(gravitationalForces);
    val macroscopicResolver = MacroscopicNetGravitationalForcesResolver(resolver, minimalAcceleration);
    Assert.assertEquals(macroscopicGravitationalForces, macroscopicResolver.resolve(Set(earth, particle), Second));
  }
}