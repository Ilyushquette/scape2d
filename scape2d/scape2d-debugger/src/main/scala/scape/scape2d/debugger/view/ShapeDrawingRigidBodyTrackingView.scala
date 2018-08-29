package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.graphics.CustomizedShape
import scape.scape2d.engine.geom.shape.FiniteShape

case class ShapeDrawingRigidBodyTrackingView(
  shapeDrawer:ShapeDrawer
) extends RigidBodyTrackingView {
  val rigidBodyColor = 0xFF00FF00; // GREEN
  
  def renderRigidBody(rigidBody:RigidBody[_ <: FiniteShape]) = shapeDrawer draw CustomizedShape(rigidBody.shape, rigidBodyColor);
  
  def clearRigidBody(rigidBody:RigidBody[_ <: FiniteShape]) = shapeDrawer clear rigidBody.shape;
}