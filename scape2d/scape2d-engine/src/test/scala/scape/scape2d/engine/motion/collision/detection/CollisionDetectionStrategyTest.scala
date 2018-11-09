package scape.scape2d.engine.motion.collision.detection

import java.util.Arrays

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import scape.scape2d.engine.core.mock.MovableMock

object CollisionDetectionStrategyTest {
  @Parameterized.Parameters
  def instancesToTest:java.util.List[Array[() => CollisionDetectionStrategy[MovableMock]]] = Arrays.asList(
      Array(() => IterativeRootFindingCollisionDetectionStrategy())
  );
}

@RunWith(classOf[Parameterized])
class CollisionDetectionStrategyTest(
  val detectionStrategyFactory:() => CollisionDetectionStrategy[MovableMock]
) {
  @Test
  def testStationariesNoCollision = {
    CollisionDetectionStrategyValidator.checkStationaryNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testSynchronousPairNoCollision = {
    CollisionDetectionStrategyValidator.checkSynchronousPairNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def testDirectCollision = {
    CollisionDetectionStrategyValidator.checkDirectCollision(detectionStrategyFactory());
  }
  
  @Test
  def testFastVersusSlowNoCollision = {
    CollisionDetectionStrategyValidator.checkFastVersusSlowNoCollision(detectionStrategyFactory());
  }
  
  @Test
  def checkSmallFastVersusLargeSlowCollision = {
    CollisionDetectionStrategyValidator.checkSmallFastVersusLargeSlowCollision(detectionStrategyFactory());
  }
}