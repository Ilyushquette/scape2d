package scape.scape2d.engine.motion.rotational

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.time.Minute
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Hour

class AngularVelocityTest {
  @Test
  def testDifferentAngularVelocities = {
    Assert.assertFalse(10(Degree) / Second == UnboundAngle(-5, Degree) / Duration(500, Millisecond));
  }
  
  @Test
  def testSameAngularVelocities = {
    Assert.assertTrue(Angle.full / Hour == Angle.straight / Duration(30, Minute));
  }
}