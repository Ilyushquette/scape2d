package scape.scape2d.engine.motion.collision.detection.rotation

import java.lang.Math.abs
import java.lang.Math.min
import com.google.common.math.DoubleMath
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.rotational.distanceForTimeOf
import scape.scape2d.engine.motion.rotational.trajectory.crossPaths
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle

class IterativeRootFindingRotationalCollisionDetectionStrategy[T <: Movable with Formed[Circle]]
extends RotationalCollisionDetectionStrategy[T] {
  def detect(P:T, Q:T, timestep:Double) = {
    if(P.rotatable != Q.rotatable && crossPaths(P, Q)) {
      val Protates = P.rotatable.map(_.angularVelocity != 0).getOrElse(false);
      val Qrotates = Q.rotatable.map(_.angularVelocity != 0).getOrElse(false);
      if(Protates && Qrotates) detectBetweenRotatables(P, Q, timestep);
      else if(Protates && !Qrotates) detectBetweenRotatableAndStationary(P, Q, timestep);
      else detectBetweenRotatableAndStationary(Q, P, timestep);
    }else None;
  }
  
  private def detectBetweenRotatables[T <: Movable with Formed[Circle]](P:T, Q:T, timestep:Double) = {
    val minimalStepDistance = min(P.radius, Q.radius);
    val Pstep = asRotatableRelativeRadians(minimalStepDistance, P);
    val Qstep = asRotatableRelativeRadians(minimalStepDistance, Q);
    val Ptimestep = timestepForRadians(P, Pstep);
    val Qtimestep = timestepForRadians(Q, Qstep);
    val minimalTimestep = min(min(Qtimestep, Ptimestep), timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, minimalTimestep, 0, timestep);
  }
  
  private def detectBetweenRotatableAndStationary[T <: Movable with Formed[Circle]](P:T, Q:T, timestep:Double) = {
    val minimalStepDistance = min(P.radius, Q.radius);
    val Pstep = asRotatableRelativeRadians(minimalStepDistance, P);
    val Ptimestep = timestepForRadians(P, Pstep);
    val minimalTimestep = min(Ptimestep, timestep);
    val smallRadii = P.radius + Q.radius;
    val ft = distanceForTimeOf(P, Q) andThen (_ - smallRadii);
    approximateCollisionTime(ft, minimalTimestep, 0, timestep);
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
  
  private def asRotatableRelativeRadians(r:Double, M:Movable) = {
    val R = M.rotatable.get;
    r / (R.center distanceTo M.position);
  }
  
  private def timestepForRadians(movable:Movable, radians:Double) = {
    abs(radians / movable.rotatable.get.angularVelocity * 1000);
  }
}