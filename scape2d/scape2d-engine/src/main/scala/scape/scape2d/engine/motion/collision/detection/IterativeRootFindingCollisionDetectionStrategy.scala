package scape.scape2d.engine.motion.collision.detection

import java.lang.Math.{min => minNumeric}
import java.lang.Math.abs
import com.google.common.math.DoubleMath
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed.exposeShape
import scape.scape2d.engine.motion.distanceForTimeOf
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.motion.collision.detection.linear.LinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.RotationalCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.rotation.IterativeRootFindingRotationalCollisionDetectionStrategy
import scape.scape2d.engine.motion.rotational.trajectory.trajectoryCircleOf
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.util.comparison.min

case class IterativeRootFindingCollisionDetectionStrategy[T <: Movable with Formed[Circle]](
  linearStrategy:LinearMotionCollisionDetectionStrategy[T] = QuadraticLinearMotionCollisionDetectionStrategy[T](),
  angularStrategy:RotationalCollisionDetectionStrategy[T] = IterativeRootFindingRotationalCollisionDetectionStrategy[T]()
) extends CollisionDetectionStrategy[T] {
  def detect(P:T, Q:T, timestep:Duration):Option[Duration] = {
    if(P.isStationary && Q.isStationary) {
      None;
    }else if((!P.isRotating && !Q.isRotating) || (P.rotatable == Q.rotatable)) {
      linearStrategy.detect(P, Q, timestep);
    }else if(!P.isMovingLinearly && !Q.isMovingLinearly) {
      angularStrategy.detect(P, Q, timestep);
    }else {
      val minimalStepDistance = minNumeric(P.radius, Q.radius);
      val PminimalTimestep = timestepForDistance(P, minimalStepDistance);
      val QminimalTimestep = timestepForDistance(Q, minimalStepDistance);
      val minimalTimestep = min(PminimalTimestep, QminimalTimestep, timestep);
      val smallRadii = P.radius + Q.radius;
      val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
      approximateCollisionTime(ft, minimalTimestep, Duration.zero, timestep);
    }
  }
  
  private def approximateCollisionTime(ft:Duration => Double,
                                       subTimestep:Duration,
                                       approximatedTime:Duration,
                                       timestep:Duration):Option[Duration] = {
    val t = approximatedTime + subTimestep;
    val distance = ft(t);
    val nextSubtimestep = min(timestep - t, subTimestep);
    if(distance <= 0) Some(approximatedTime);
    else if(nextSubtimestep > Duration.zero)
      approximateCollisionTime(ft, nextSubtimestep, t, timestep);
    else
      None;
  }
  
  private def timestepForDistance(movable:Movable, distance:Double) = {
    val combinedVelocitiesScalar = combineAngularAndLinearVelocitiesScalar(movable);
    Duration(distance / combinedVelocitiesScalar, Second);
  }
  
  private def combineAngularAndLinearVelocitiesScalar(movable:Movable) = {
    val linearVelocityMagnitude = movable.velocity.forTime(Second).magnitude;
    
    if(movable.isRotating) {
      val ωt = movable.rotatable.get.angularVelocity.forTime(Second);
      val angularVelocityMagnitude = abs(trajectoryCircleOf(movable).forAngle(ωt));
      linearVelocityMagnitude + angularVelocityMagnitude;
    } else linearVelocityMagnitude;
  }
}