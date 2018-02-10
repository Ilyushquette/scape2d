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

class MotionFunctionsTest {
  @Test
  def testPositionForTime = {
    val movable = new MovableMock(Point(-1, 0), Vector.from(Components(3, 1)), None);
    val rotatable = new RotatableMock(Point.origin, -PI, Set(movable));
    Assert.assertEquals(Point(4, 1), positionForTimeOf(movable)(1000));
  }
  
  @Test
  def testDistanceForTime = {
    val movable1 = new MovableMock(Point(-1, 0), Vector.from(Components(3, 0)), None);
    val rotatable1 = new RotatableMock(Point.origin, -PI, Set(movable1));
    val movable2 = new MovableMock(Point(11, 0), Vector.from(Components(-3, 0)), None);
    val rotatable2 = new RotatableMock(Point(10, 0), PI, Set(movable2));
    Assert.assertEquals(7, distanceForTimeOf(movable1, movable2)(500), Epsilon);
  }
}