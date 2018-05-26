package scape.scape2d.engine.motion.linear

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.core.MovableMock

class LinearMotionFunctionsTest {
  @Test
  def testPostLinearMotionPositionZeroDisplacement = {
    val movableMock = new MovableMock(Point(3, 5), Velocity.zero, None);
    Assert.assertEquals(Point(3, 5), positionForTimeOf(movableMock)(Duration(100, Millisecond)));
  }
  
  @Test
  def testPostLinearMotionPosition = {
    val movableMock = new MovableMock(Point(3, 5), Vector.from(Components(-3, -5)) / Second, None);
    Assert.assertEquals(Point(2.7, 4.5), positionForTimeOf(movableMock)(Duration(100, Millisecond)))
  }
}