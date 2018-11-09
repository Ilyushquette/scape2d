package scape.scape2d.engine.motion.collision.detection.linear

import java.util.Arrays

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import scape.scape2d.engine.core.mock.MovableMock
import scape.scape2d.engine.motion.collision.detection.IterativeRootFindingCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.{LinearMotionCollisionDetectionStrategyValidator => DetectionStrategyValidator}

object LinearMotionCollisionDetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest:java.util.List[Array[() => LinearMotionCollisionDetectionStrategy[MovableMock]]] = Arrays.asList(
      Array(() => QuadraticLinearMotionCollisionDetectionStrategy()),
      Array(() => IterativeRootFindingCollisionDetectionStrategy())
  );
}

@RunWith(classOf[Parameterized])
class LinearMotionCollisionDetectionStrategyTest(
  val createDetector:() => LinearMotionCollisionDetectionStrategy[MovableMock]
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