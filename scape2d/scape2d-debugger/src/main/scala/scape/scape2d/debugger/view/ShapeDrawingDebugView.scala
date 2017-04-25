package scape.scape2d.debugger.view

import scape.scape2d.engine.matter.MotionEvent
import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom.Point2D

class ShapeDrawingDebugView(val shapeDrawer:ShapeDrawer, val cmpp:Double) extends DebugView {
  private val mpp = cmpp / 100;
  
  override def renderParticle(particle:Particle) = {
    val scaledRadius = scaleRadius(particle.radius);
    val scaledCoords = scaleCoordinates(particle.position);
    shapeDrawer.draw(Circle(scaledCoords._1, scaledCoords._2, scaledRadius));
  }
  
  override def renderMotion(event:MotionEvent) = {
    val oldScaledCoords = scaleCoordinates(event.oldPosition);
    val newScaledCoords = scaleCoordinates(event.updatedParticle.position);
    val scaledRadius = scaleRadius(event.updatedParticle.radius);
    val clearable = Circle(oldScaledCoords._1, oldScaledCoords._2, scaledRadius);
    val drawable = Circle(newScaledCoords._1, newScaledCoords._2, scaledRadius);
    shapeDrawer.clearAndDraw(clearable, drawable);
  }
  
  private def scaleCoordinates(position:Point2D) = ((position.x / mpp).toInt, (position.y / mpp).toInt);
  
  private def scaleRadius(radius:Double) = radius / cmpp;
}