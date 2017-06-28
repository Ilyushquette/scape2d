package scape.scape2d.engine.motion.collision.detection

import java.util.Arrays

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

object DetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest = Arrays.asList(Array(() => detectWithDiscriminant _));
}

@RunWith(classOf[Parameterized])
class DetectionStrategyTest(val createDetector:() => DetectionStrategy) {
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