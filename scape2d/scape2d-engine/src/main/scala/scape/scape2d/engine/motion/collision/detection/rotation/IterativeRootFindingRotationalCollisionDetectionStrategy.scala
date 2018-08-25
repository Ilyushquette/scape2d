package scape.scape2d.engine.motion.collision.detection.rotation

import java.lang.Math.abs
import java.lang.Math.{min => minNumeric}
import java.lang.Math.signum
import com.google.common.math.DoubleMath
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.rotational.distanceForTimeOf
import scape.scape2d.engine.motion.rotational.trajectory.crossPaths
import scape.scape2d.engine.motion.rotational.trajectory.trajectoryCircleOf
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.comparison.min
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.geom.angle.UnboundAngle

case class IterativeRootFindingRotationalCollisionDetectionStrategy[T <: Movable[Circle]]()
extends RotationalCollisionDetectionStrategy[T] {
  def detect(P:T, Q:T, timestep:Duration) = {
    if(P.rotatable != Q.rotatable && crossPaths(P, Q)) {
      if(P.isRotating && Q.isRotating) detectBetweenRotatables(P, Q, timestep);
      else if(P.isRotating && !Q.isRotating) detectBetweenRotatableAndStationary(P, Q, timestep);
      else detectBetweenRotatableAndStationary(Q, P, timestep);
    }else None;
  }
  
  private def detectBetweenRotatables(P:T, Q:T, timestep:Duration) = {
    val minimalStepDistance = minNumeric(P.radius, Q.radius);
    val PminimalTimestep = timestepForRotatedLength(P, minimalStepDistance);
    val QminimalTimestep = timestepForRotatedLength(Q, minimalStepDistance);
    val subTimestep = min(PminimalTimestep, QminimalTimestep, timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, subTimestep, Duration.zero, timestep);
  }
  
  private def detectBetweenRotatableAndStationary(P:T, Q:T, timestep:Duration) = {
    val minimalStepDistance = minNumeric(P.radius, Q.radius);
    val PminimalTimestep = timestepForRotatedLength(P, minimalStepDistance);
    val subTimestep = min(PminimalTimestep, timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, subTimestep, Duration.zero, timestep);
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
  
  private def timestepForRotatedLength(movable:T, length:Double) = {
    val angularVelocity = movable.rotatable.get.angularVelocity;
    val angularDistance = trajectoryCircleOf(movable).forLength(length);
    val rotatedAngularDistance = angularDistance * signum(angularVelocity.angle.value);
    angularVelocity.forAngle(rotatedAngularDistance);
  }
}