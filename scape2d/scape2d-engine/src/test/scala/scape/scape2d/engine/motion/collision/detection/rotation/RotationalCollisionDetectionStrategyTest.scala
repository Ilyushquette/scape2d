package scape.scape2d.engine.motion.collision.detection.rotation

import java.util.Arrays
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import scape.scape2d.engine.core.MovableMock
import scape.scape2d.engine.motion.collision.detection.rotation.{RotationalCollisionDetectionStrategyValidator => Validator}

object RotationalCollisionDetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest = Arrays.asList(Array(
      () => new IterativeRootFindingRotationalCollisionDetectionStrategy
  ));
}

@RunWith(classOf[Parameterized])
class RotationalCollisionDetectionStrategyTest(
  val detectionStrategyFactory:() => RotationalCollisionDetectionStrategy[MovableMock]) {
  
  @Test
  def testNonRotatablesNoCollision = {
    Validator.checkNonRotatablesNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testTrajectoriesDontIntersectNoCollision = {
    Validator.checkTrajectoriesDontIntersectNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testTurnRoundEachOtherNoCollision = {
    Validator.checkTurnRoundEachOtherNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testDirectCollision = {
    Validator.checkDirectCollision(detectionStrategyFactory());
  }
  
  @Test
  def testSlowVersusTwiceAsFastNoCollision = {
    Validator.checkSlowVersusTwiceAsFastNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testLargeFastRotatableWithSmallNonRotatableCollision = {
    Validator.checkLargeFastRotatableWithSmallNonRotatableCollision(detectionStrategyFactory());
  }
  
  @Test
  def testSmallSlowRotatableWithLargeNonRotatableCollision = {
    Validator.checkSmallSlowRotatableWithLargeNonRotatableCollision(detectionStrategyFactory());
  }
}