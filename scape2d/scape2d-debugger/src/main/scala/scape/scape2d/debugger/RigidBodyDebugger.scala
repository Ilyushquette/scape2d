package scape.scape2d.debugger

import scape.scape2d.debugger.view.RigidBodyTrackingView
import scape.scape2d.engine.core.MotionEvent
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.util.Proxy.autoEnhance

case class RigidBodyDebugger(
  rigidBodyTrackingView:RigidBodyTrackingView
) {
  def trackRigidBody[T >: Null <: FiniteShape](trackedRigidBody:MovableTrackerProxy[RigidBody[T]]) = {
    rigidBodyTrackingView renderRigidBody trackedRigidBody;
    trackedRigidBody onMotion renderMotion;
  }
  
  def renderMotion[T >: Null <: FiniteShape](motion:MotionEvent[RigidBody[T]]) = {
    rigidBodyTrackingView.clearRigidBody(motion.old);
    rigidBodyTrackingView.renderRigidBody(motion.snapshot);
  }
}