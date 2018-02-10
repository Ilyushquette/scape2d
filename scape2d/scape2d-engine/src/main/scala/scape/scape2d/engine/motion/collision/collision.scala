package scape.scape2d.engine.motion

import scape.scape2d.engine.geom._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.util.LazyVal
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.linear.asMetersPerTimestep

package object collision {
  def findSafeTime[T <: Movable with Formed[_]](collision:CollisionEvent[T], closestDistance:Double) = {
    val snapshotPair = collision.snapshotPair;
    val faster = Seq(snapshotPair._1, snapshotPair._2).maxBy(_.velocity.magnitude);
    val scaledVelocity = asMetersPerTimestep(faster.velocity, collision.time);
    val distance = scaledVelocity.magnitude * collision.time;
    if(distance > closestDistance) {
      val safeDistance = distance - closestDistance;
      safeDistance / scaledVelocity.magnitude;
    }else 0;
  }
}