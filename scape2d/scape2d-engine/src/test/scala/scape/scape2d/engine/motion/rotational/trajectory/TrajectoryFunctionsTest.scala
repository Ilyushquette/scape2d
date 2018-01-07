package scape.scape2d.engine.motion.rotational.trajectory

import java.util.NoSuchElementException

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.core.RotatableMock
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ring

class TrajectoryFunctionsTest {
  @Test(expected = classOf[NoSuchElementException])
  def testTrajectoryOfNonRotatable:Unit = {
    val movable = new MovableMock(Circle(Point.origin, 0.1), Vector(), None);
    trajectoryOf(movable)
  }
  
  @Test
  def testTrajectoryOfRotatable = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Vector(), None);
    val rotatable = new RotatableMock(Point(1, 1), 0, Set(movable));
    Assert.assertEquals(Ring(Circle(Point(1, 1), 5), 1), trajectoryOf(movable));
  }
  
  @Test
  def testPathOfNonRotatableIsCurrentlyConsumedArea = {
    val movable = new MovableMock(Circle(Point.origin, 0.1), Vector(), None);
    Assert.assertEquals(movable.shape, pathOf(movable));
  }
  
  @Test
  def testPathOfStationaryIsCurrentlyConsumedArea = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Vector(), None);
    val rotatable = new RotatableMock(Point(1, 1), 0, Set(movable));
    Assert.assertEquals(movable.shape, pathOf(movable));
  }
  
  @Test
  def testPathOfRotatableIsTrajectoryOfRotation = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Vector(), None);
    val rotatable = new RotatableMock(Point(1, 1), 2.35, Set(movable));
    Assert.assertEquals(Ring(Circle(Point(1, 1), 5), 1), pathOf(movable));
  }
}