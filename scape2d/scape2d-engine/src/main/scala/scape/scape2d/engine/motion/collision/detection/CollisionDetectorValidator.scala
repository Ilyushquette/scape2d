package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.MovableMock

object CollisionDetectorValidator {
  def check(detector:CollisionDetector[MovableMock]) = {
    val strategyAdaptedDetector = CollisionDetectorStrategyAdapter(detector);
    CollisionDetectionStrategyValidator.check(strategyAdaptedDetector);
  }
}