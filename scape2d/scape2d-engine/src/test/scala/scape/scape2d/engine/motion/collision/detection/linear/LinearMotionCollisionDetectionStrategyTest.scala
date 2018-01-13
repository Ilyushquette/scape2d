package scape.scape2d.engine.motion.collision.detection.linear

import java.util.Arrays
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import scape.scape2d.engine.motion.collision.detection.linear.{LinearMotionCollisionDetectionStrategyValidator => DetectionStrategyValidator}

object LinearMotionCollisionDetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest = Arrays.asList(Array(
      () => new QuadraticLinearMotionCollisionDetectionStrategy
  ));
}

@RunWith(classOf[Parameterized])
class LinearMotionCollisionDetectionStrategyTest(
  val createDetector:() => LinearMotionCollisionDetectionStrategy[Mock]
) {
  @Test
  def testTrajectoriesOverlayFrontalCollision = {
    DetectionStrategyValidator.checkTrajectoriesOverlayFrontalCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayFrontalNoCollision = {
    DetectionStrategyValidator.checkTrajectoriesOverlayFrontalNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayUnidirectionalNoCollision = {
    DetectionStrategyValidator.checkTrajectoriesOverlayUnidirectionalNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayUnidirectionalCollision = {
    DetectionStrategyValidator.checkTrajectoriesOverlayUnidirectionalCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesCrossedNoCollision = {
    DetectionStrategyValidator.checkTrajectoriesCrossedNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesCrossedCollision = {
    DetectionStrategyValidator.checkTrajectoriesCrossedCollision(createDetector());
  }
}