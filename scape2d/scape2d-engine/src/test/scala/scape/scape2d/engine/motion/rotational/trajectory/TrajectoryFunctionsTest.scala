package scape.scape2d.engine.motion.rotational.trajectory

import java.util.NoSuchElementException
import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.core.mock.MovableMock
import scape.scape2d.engine.core.mock.RotatableMock
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle

class TrajectoryFunctionsTest {
  @Test(expected = classOf[NoSuchElementException])
  def testTrajectoryCircleOfNonRotatable:Unit = {
    val movable = new MovableMock(Circle(Point.origin, 0.1), Velocity.zero, None);
    trajectoryCircleOf(movable);
  }
  
  @Test
  def testTrajectoryCircleOfRotatable = {
    val movable = new MovableMock(Circle(Point(3, 5), 2), Velocity.zero, None);
    val rotatable = new RotatableMock(Point(3, 10), AngularVelocity.zero, Set(movable));
    Assert.assertEquals(Circle(Point(3, 10), 5), trajectoryCircleOf(movable));
  }
  
  @Test
  def testTrajectoryOfRotatable = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Velocity.zero, None);
    val rotatable = new RotatableMock(Point(1, 1), AngularVelocity.zero, Set(movable));
    Assert.assertEquals(Ring(Circle(Point(1, 1), 5), 1), trajectoryOf(movable));
  }
  
  @Test
  def testPathOfNonRotatableIsCurrentlyConsumedArea = {
    val movable = new MovableMock(Circle(Point.origin, 0.1), Velocity.zero, None);
    Assert.assertEquals(movable.shape, pathOf(movable));
  }
  
  @Test
  def testPathOfStationaryIsCurrentlyConsumedArea = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Velocity.zero, None);
    val rotatable = new RotatableMock(Point(1, 1), AngularVelocity.zero, Set(movable));
    Assert.assertEquals(movable.shape, pathOf(movable));
  }
  
  @Test
  def testPathOfRotatableIsTrajectoryOfRotation = {
    val movable = new MovableMock(Circle(Point(1, 6), 0.5), Velocity.zero, None);
    val rotatable = new RotatableMock(Point(1, 1), 2.35(Radian) / Second, Set(movable));
    Assert.assertEquals(Ring(Circle(Point(1, 1), 5), 1), pathOf(movable));
  }
  
  @Test
  def testCrossPathsNonRotatablesNoIntersection = {
    val movable1 = new MovableMock(Circle(Point(0, 1), 0.1), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point.origin, 0.1), Velocity.zero, None);
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatableWithNonRotatableIntersection = {
    val movable1 = new MovableMock(Circle(Point(0, 1), 0.1), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(0, -1), 0.1), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, 0.5(Radian) / Second, Set(movable1));
    Assert.assertTrue(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatableWithNonRotatableNoIntersection = {
    val movable1 = new MovableMock(Circle(Point(0, 1), 0.1), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(0, -1.5), 0.1), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, 0.5(Radian) / Second, Set(movable1));
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsStationariesNoIntersection = {
    val movable1 = new MovableMock(Circle(Point(1, 0), 0.1), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(-2, 0), 0.1), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, AngularVelocity.zero, Set(movable1));
    val rotatable2 = new RotatableMock(Point(-1, 0), AngularVelocity.zero, Set(movable2));
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatableWithStationaryIntersection = {
    val movable1 = new MovableMock(Circle(Point.origin, 0.1), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point(1, 0), AngularVelocity.zero, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(-2, 0), 0.1), Velocity.zero, None);
    val rotatable2 = new RotatableMock(Point(-1, 0), UnboundAngle(-0.5, Radian) / Second, Set(movable2));
    Assert.assertTrue(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatableWithStationaryNoIntersection = {
    val movable1 = new MovableMock(Circle(Point(3, 0), 0.1), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(-2, 0), 0.1), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point(2, 0), AngularVelocity.zero, Set(movable1));
    val rotatable2 = new RotatableMock(Point(-1, 0), 0.5(Radian) / Second, Set(movable2));
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatablesIntersection = {
    val movable1 = new MovableMock(Circle(Point(-3, 0), 0.5), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, 0.3(Radian) / Second, Set(movable1));
    val movable2 = new MovableMock(Circle(Point(7, -3), 0.5), Velocity.zero, None);
    val rotatable2 = new RotatableMock(Point(7, 0), UnboundAngle(-0.3, Radian) / Second, Set(movable2));
    Assert.assertTrue(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatablesNoIntersectionSmallRadiiDecreased = {
    val movable1 = new MovableMock(Circle(Point(-3, 0), 0.4), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(7, -3), 0.4), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, 0.3(Radian) / Second, Set(movable1));
    val rotatable2 = new RotatableMock(Point(7, 0), UnboundAngle(-0.3, Radian) / Second, Set(movable2));
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
  
  @Test
  def testCrossPathsRotatablesNoIntersectionLargeRadiusDecreased = {
    val movable1 = new MovableMock(Circle(Point(-2.9, 0), 0.5), Velocity.zero, None);
    val movable2 = new MovableMock(Circle(Point(7, -3), 0.5), Velocity.zero, None);
    val rotatable1 = new RotatableMock(Point.origin, 0.3(Radian) / Second, Set(movable1));
    val rotatable2 = new RotatableMock(Point(7, 0), UnboundAngle(-0.3, Radian) / Second, Set(movable2));
    Assert.assertFalse(crossPaths(movable1, movable2));
  }
}