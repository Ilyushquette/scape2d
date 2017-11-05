package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.scaleVelocity
import scape.scape2d.engine.motion.collision.detection.DetectionStrategyValidator
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

package object detection {
  type MovableSphere = Movable with Formed[Circle];
  type DetectionStrategy = (MovableSphere, MovableSphere, Double) => Option[Double];
  
  def detectWithDiscriminant[T <: MovableSphere](s1:T, s2:T, timestep:Double) = {
    val sumOfRadii = s1.shape.radius + s2.shape.radius;
    val A = s1.position - s2.position;
    val B = scaleVelocity(s1.velocity, timestep) - scaleVelocity(s2.velocity, timestep);
    
    val a = B * B;
    val b = 2 * (A * B);
    val c = (A * A) - (sumOfRadii * sumOfRadii);
    
    val discriminant = (b * b) - (4 * a * c);
    if(discriminant > 0) {
      val coefficient = (-b - Math.sqrt(discriminant)) / (2 * a);
      if(coefficient > 0 && coefficient <= 1) Some(coefficient * timestep);
      else None;
    }else None;
  }
}