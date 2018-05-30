package scape.scape2d.engine.motion

import java.lang.Math.PI
import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.doubleToTime
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.Angle

class MotionFunctionsTest {
  @Test
  def testPositionForTime = {
    val movable = new MovableMock(Point(-1, 0), Vector.from(Components(3, 1)) / Second, None);
    val rotatable = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable));
    Assert.assertEquals(Point(4, 1), positionForTimeOf(movable)(Second));
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(-1, 0), Vector.from(Components(3, 0)) / Second, None);
    val rotatable1 = new RotatableMock(Point.origin, UnboundAngle(-PI, Radian) / Second, Set(movable1));
    val movable2 = new MovableMock(Point(11, 0), Vector.from(Components(-3, 0)) / Second, None);
    val rotatable2 = new RotatableMock(Point(10, 0), Angle.straight / Second, Set(movable2));
    Assert.assertEquals(7, distanceForTimeOf(movable1, movable2)(500(Millisecond)), Epsilon);
  }
}