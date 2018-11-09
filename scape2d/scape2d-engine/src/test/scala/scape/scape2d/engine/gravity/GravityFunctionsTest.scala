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

class GravityFunctionsTest {
  @Test
  def testGravitationalForceOntoBall = {
    val earth = new MatterMock(
        shape = Circle(Point.origin, 6371000),
        mass = Mass(5.972E24, Kilogram)
    );
    val ball = new MatterMock(
        shape = Circle(Point(0, 6371050), 1),
        mass = Mass(3, Kilogram)
    );
    Assert.assertEquals(Vector(29.4581337181, 270(Degree)), earth.gravitationalForceOnto(ball));
  }
  
  @Test
  def testGravitationalForceOntoSelfIsZero = {
    val ball = new MatterMock(
        shape = Circle(Point(0, 6371050), 1),
        mass = Mass(3, Kilogram)
    );
    Assert.assertEquals(Vector.zero, ball.gravitationalForceOnto(ball));
  }
}