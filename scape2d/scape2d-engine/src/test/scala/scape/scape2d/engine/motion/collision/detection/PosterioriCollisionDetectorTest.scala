package scape.scape2d.engine.motion.collision.detection

import org.junit.Test
import org.junit.runners.Parameterized
import java.util.Arrays
import org.junit.runner.RunWith
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import org.junit.Assert
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.time.Duration

object PosterioriCollisionDetectorTest {
  @Parameterized.Parameters
  def instancesToTest:java.util.List[Array[() => PosterioriCollisionDetector[MovableMock]]] = Arrays.asList(
      Array(() => BruteForcePosterioriCollisionDetector()),
      Array(() => TreePosterioriCollisionDetector(
          () => new QuadTree[MovableMock](AxisAlignedRectangle(Point(-20, -20), 40, 40), 3)
      ))
  );
}

@RunWith(classOf[Parameterized])
class PosterioriCollisionDetectorTest(
  val collisionDetectorFactory:() => PosterioriCollisionDetector[MovableMock]
) {
  @Test
  def testNoMovablesNoCollisionDetections = {
    val collisionDetector = collisionDetectorFactory();
    val collisions = collisionDetector.detect(Set());
    Assert.assertEquals(Set(), collisions);
  }
  
  @Test
  def testCollisionsBetweenMovablesDetection = {
    val collisionDetector = collisionDetectorFactory();
    val movable1 = new MovableMock(Circle(Point.origin, 3), Vector.zero, None);
    val movable2 = new MovableMock(Circle(Point(0, 5), 3), Vector.zero, None);
    val movable3 = new MovableMock(Circle(Point.origin, 1), Vector.zero, None);
    val movable4 = new MovableMock(Circle(Point(-10, 10), 2), Vector.zero, None);
    val movables = Set(movable1, movable2, movable3, movable4);
    val expectedCollisions = Set(CollisionEvent(Combination2(movable1, movable2), Duration.zero),
                                 CollisionEvent(Combination2(movable1, movable3), Duration.zero));
    val collisions = collisionDetector.detect(movables);
    Assert.assertEquals(expectedCollisions, collisions);
  }
}