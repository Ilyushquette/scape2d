package scape.scape2d.benchmark

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.collision.Collision
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
class CollisionDetectionBenchmark {
  @Param(Array("10", "100", "1000"))
  var mocksCount:Int = 0;
  var mocks:Iterable[Mock] = null;
  var bruteForceWithDiscriminantDetector:BruteForceBasedCollisionDetector[Mock] = null;
  
  @Setup
  def prepare = {
    mocks = for(i <- 1 to mocksCount) yield new Mock(5, Point(i, i - 10), new Vector2D(i, i));
    bruteForceWithDiscriminantDetector = new BruteForceBasedCollisionDetector[Mock](detectWithDiscriminant _);
  }
  
  @Benchmark
  def measureBruteForceWithDiscriminantStrategy(blackhole:Blackhole) {
    blackhole.consume(bruteForceWithDiscriminantDetector.detect(mocks, 16));
  }
}

private[benchmark] class Mock(val radius:Double, val position:Point, val velocity:Vector2D)
extends Movable[Mock] with Formed[Circle] {
  def setPosition(nextPosition:Point) = {}
  def setVelocity(newVelocity:Vector2D) = {}
  def shape = Circle(position, radius);
  def snapshot = this;
}