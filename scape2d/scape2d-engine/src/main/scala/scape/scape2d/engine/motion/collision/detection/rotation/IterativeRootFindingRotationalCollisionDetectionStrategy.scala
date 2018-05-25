package scape.scape2d.engine.motion.collision.detection.rotation

import java.lang.Math.abs
import java.lang.Math.{min => minNumeric}
import com.google.common.math.DoubleMath
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.rotational.distanceForTimeOf
import scape.scape2d.engine.motion.rotational.trajectory.crossPaths
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.comparison.min
import scape.scape2d.engine.time.Second

case class IterativeRootFindingRotationalCollisionDetectionStrategy[T <: Movable with Formed[Circle]]()
extends RotationalCollisionDetectionStrategy[T] {
  def detect(P:T, Q:T, timestep:Duration) = {
    if(P.rotatable != Q.rotatable && crossPaths(P, Q)) {
      if(P.isRotating && Q.isRotating) detectBetweenRotatables(P, Q, timestep);
      else if(P.isRotating && !Q.isRotating) detectBetweenRotatableAndStationary(P, Q, timestep);
      else detectBetweenRotatableAndStationary(Q, P, timestep);
    }else None;
  }
  
  private def detectBetweenRotatables[T <: Movable with Formed[Circle]](P:T, Q:T, timestep:Duration) = {
    val minimalStepDistance = minNumeric(P.radius, Q.radius);
    val Pstep = asRotatableRelativeRadians(minimalStepDistance, P);
    val Qstep = asRotatableRelativeRadians(minimalStepDistance, Q);
    val Ptimestep = timestepForRadians(P, Pstep);
    val Qtimestep = timestepForRadians(Q, Qstep);
    val minimalTimestep = min(Qtimestep, Ptimestep, timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, minimalTimestep, Duration.zero, timestep);
  }
  
  private def detectBetweenRotatableAndStationary[T <: Movable with Formed[Circle]](P:T, Q:T, timestep:Duration) = {
    val minimalStepDistance = minNumeric(P.radius, Q.radius);
    val Pstep = asRotatableRelativeRadians(minimalStepDistance, P);
    val Ptimestep = timestepForRadians(P, Pstep);
    val minimalTimestep = min(Ptimestep, timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, minimalTimestep, Duration.zero, timestep);
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
  
  private def asRotatableRelativeRadians(r:Double, M:Movable) = {
    val R = M.rotatable.get;
    r / (R.center distanceTo M.position);
  }
  
  private def timestepForRadians(movable:Movable, radians:Double) = {
    Duration(abs(radians / movable.rotatable.get.angularVelocity), Second);
  }
}