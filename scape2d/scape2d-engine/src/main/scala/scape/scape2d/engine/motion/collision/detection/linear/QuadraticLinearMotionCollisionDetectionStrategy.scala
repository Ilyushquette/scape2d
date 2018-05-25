package scape.scape2d.engine.motion.collision.detection.linear

import java.lang.Math.sqrt

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.linear.asMetersPerTimestep
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

case class QuadraticLinearMotionCollisionDetectionStrategy[T <: Movable with Formed[Circle]](
  closestDistance:Double = Epsilon
) extends LinearMotionCollisionDetectionStrategy[T] {
  def detect(s1:T, s2:T, timestep:Double) = {
    val contactSafeDistance = s1.radius + s2.radius + closestDistance;
    val A = s1.position - s2.position;
    val B = asMetersPerTimestep(s1.velocity, timestep) - asMetersPerTimestep(s2.velocity, timestep);
    
    val a = B * B;
    val b = 2 * (A * B);
    val c = (A * A) - (contactSafeDistance * contactSafeDistance);
    
    val discriminant = (b * b) - (4 * a * c);
    if(discriminant > 0) {
      val coefficient = (-b - sqrt(discriminant)) / (2 * a);
      if(coefficient > 0 && coefficient <= 1) Some(coefficient * timestep);
      else None;
    }else None;
  }
}