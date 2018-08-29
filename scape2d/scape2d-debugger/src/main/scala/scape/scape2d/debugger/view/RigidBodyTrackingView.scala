package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.geom.shape.FiniteShape

trait RigidBodyTrackingView {
  def renderRigidBody(rigidBody:RigidBody[_ <: FiniteShape]);
  
  def clearRigidBody(rigidBody:RigidBody[_ <: FiniteShape]);
}