package scape.scape2d.engine.motion.collision.detection

import java.lang.Math.min
import java.lang.Math.abs
import com.google.common.math.DoubleMath
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed.exposeShape
import scape.scape2d.engine.motion.distanceForTimeOf
import scape.scape2d.engine.motion.rotational.angularToLinearVelocityScalar
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.IterativeRootFindingRotationalCollisionDetectionStrategy

case class IterativeRootFindingCollisionDetectionStrategy[T <: Movable with Formed[Circle]](
  linearStrategy:LinearMotionCollisionDetectionStrategy[T] = QuadraticLinearMotionCollisionDetectionStrategy[T](),
  angularStrategy:RotationalCollisionDetectionStrategy[T] = IterativeRootFindingRotationalCollisionDetectionStrategy[T]()
) extends CollisionDetectionStrategy[T] {
  def detect(P:T, Q:T, timestep:Double):Option[Double] = {
    if(P.isStationary && Q.isStationary) {
      None;
    }else if((!P.isRotating && !Q.isRotating) || (P.rotatable == Q.rotatable)) {
      linearStrategy.detect(P, Q, timestep);
    }else if(!P.isMovingLinearly && !Q.isMovingLinearly) {
      angularStrategy.detect(P, Q, timestep);
    }else {
      val minimalStepDistance = min(P.radius, Q.radius);
      val PminimalTimestep = minimalTimestepForDistance(P, minimalStepDistance);
      val QminimalTimestep = minimalTimestepForDistance(Q, minimalStepDistance);
      val minimalTimestep = min(min(PminimalTimestep, QminimalTimestep), timestep);
      val smallRadii = P.radius + Q.radius;
      val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
      approximateCollisionTime(ft, minimalTimestep, 0, timestep);
    }
  }
  
  private def approximateCollisionTime(ft:Double => Double, tStep:Double, from:Double, until:Double):Option[Double] = {
    val t = from + tStep;
    val distance = ft(t);
    val nextStep = min(until - t, tStep);
    if(distance <= 0) Some(from);
    else if(!DoubleMath.fuzzyEquals(nextStep, 0, 0.00001))
      approximateCollisionTime(ft, nextStep, t, until);
    else
      None;
  }
  
  private def minimalTimestepForDistance(movable:Movable, distance:Double) = {
    val combinedVelocityScalar = combineAngularAndLinearVelocitiesOf(movable);
    distance / combinedVelocityScalar;
  }
  
  private def combineAngularAndLinearVelocitiesOf(movable:Movable) = {
    if(movable.rotatable.isDefined)
      abs(movable.velocity.magnitude + angularToLinearVelocityScalar(movable));
    else
      abs(movable.velocity.magnitude);
  }
}