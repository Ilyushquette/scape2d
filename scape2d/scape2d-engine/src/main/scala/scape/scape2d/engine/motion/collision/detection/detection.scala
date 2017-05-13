package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.motion._
import scape.scape2d.engine.geom.Spherical

package object detection {
  def detectWithDiscriminant[T <: Movable with Spherical](s1:T, s2:T, timestep:Long) = {
    val sumOfRadii = s1.radius + s2.radius;
    val A = s1.position - s2.position;
    val B = scaleVelocity(s1.velocity, timestep) - scaleVelocity(s2.velocity, timestep);
    
    val a = B * B;
    val b = 2 * (A * B);
    val c = (A * A) - (sumOfRadii * sumOfRadii);
    
    val discriminant = (b * b) - (4 * a * c);
    if(discriminant > 0) {
      val coefficient = (-b - Math.sqrt(discriminant)) / (2 * a);
      if(coefficient > 0 && coefficient <= 1) Some(coefficient);
      else None;
    }else None;
  }
}