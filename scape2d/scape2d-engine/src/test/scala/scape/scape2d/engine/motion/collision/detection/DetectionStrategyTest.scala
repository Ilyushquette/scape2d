package scape.scape2d.engine.motion.collision.detection

import java.util.Arrays

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import scape.scape2d.engine.motion.collision.DetectionStrategyValidator

object DetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest = Arrays.asList(Array(() => detectWithDiscriminant _));
}

@RunWith(classOf[Parameterized])
class DetectionStrategyTest(val createDetector:() => DetectionStrategy) {
  @Test
  def testTrajectoriesOverlayFrontalCollision = {
    validator.checkTrajectoriesOverlayFrontalCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayFrontalNoCollision = {
    validator.checkTrajectoriesOverlayFrontalNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayUnidirectionalNoCollision = {
    validator.checkTrajectoriesOverlayUnidirectionalNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesOverlayUnidirectionalCollision = {
    validator.checkTrajectoriesOverlayUnidirectionalCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesCrossedNoCollision = {
    validator.checkTrajectoriesCrossedNoCollision(createDetector());
  }
  
  @Test
  def testTrajectoriesCrossedCollision = {
    validator.checkTrajectoriesCrossedCollision(createDetector());
  }
  
  private def validator = new DetectionStrategyValidator;
}