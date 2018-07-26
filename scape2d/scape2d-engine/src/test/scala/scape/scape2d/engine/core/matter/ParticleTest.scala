package scape.scape2d.engine.core.matter

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.time.Second

class ParticleTest {
  @Test
  def testGravitationalForceOntoAnotherParticle = {
    val earth = ParticleBuilder()
                .as(Circle(Point.origin, 6371000))
                .withMass(Mass(5.972E24, Kilogram))
                .build;
    val particle = ParticleBuilder()
                   .as(Circle(Point(0, 6371050), 1))
                   .withMass(Mass(3, Kilogram))
                   .build;
    Assert.assertEquals(Vector(29.4581337181, 270(Degree)), earth.gravitationalForceOnto(particle, Second));
  }
  
  @Test
  def testGravitationalForceOntoSelfIsZero = {
    val particle = ParticleBuilder()
                   .as(Circle(Point(0, 6371050), 1))
                   .withMass(Mass(3, Kilogram))
                   .build;
    Assert.assertEquals(Vector.zero, particle.gravitationalForceOnto(particle, Second));
  }
}