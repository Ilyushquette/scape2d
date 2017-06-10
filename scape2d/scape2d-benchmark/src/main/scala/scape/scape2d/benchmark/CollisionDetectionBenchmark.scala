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
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Spherical
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.collision.Collision
import scape.scape2d.engine.motion.collision.detection._

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
class CollisionDetectionBenchmark {
  @Param(Array("10", "100", "1000"))
  var mocksCount:Int = 0;
  var mocks:Iterable[Mock] = null;
  var bruteForceWithDiscriminantStrategy:(Iterable[Mock], Double) => Iterator[Collision[Mock]] = null;
  
  @Setup
  def prepare = {
    mocks = for(i <- 1 to 10) yield new Mock(5, new Point2D(i, i - 10), new Vector2D(i, i));
    bruteForceWithDiscriminantStrategy = bruteForce[Mock](detectWithDiscriminant _);
  }
  
  @Benchmark
  def measureBruteForceWithDiscriminantStrategy(blackhole:Blackhole) {
    blackhole.consume(bruteForceWithDiscriminantStrategy(mocks, 16));
  }
}

private[benchmark] class Mock(val radius:Double, val position:Point2D, val velocity:Vector2D)
extends Movable[Mock] with Spherical {
  def setPosition(nextPosition:Point2D) = {}
  def setVelocity(newVelocity:Vector2D) = {}
  def snapshot = this;
}