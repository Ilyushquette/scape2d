package scape.scape2d.engine.motion

import scape.scape2d.engine.geom.Spherical

package object collision {
  def findSafeTime[T <: Movable with Spherical](collision:Collision[T], closestDistance:Double) = {
    val faster = Seq(collision.pair._1, collision.pair._2).maxBy(_.velocity.magnitude);
    val scaledVelocity = scaleVelocity(faster.velocity, collision.time);
    val distance = scaledVelocity.magnitude * collision.time;
    if(distance > closestDistance) {
      val safeDistance = distance - closestDistance;
      safeDistance / scaledVelocity.magnitude;
    }else 0;
  }
}