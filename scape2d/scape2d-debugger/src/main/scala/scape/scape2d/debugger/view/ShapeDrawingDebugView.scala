package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.shape.Point

class ShapeDrawingDebugView(val shapeDrawer:ShapeDrawer, val cmpp:Double) extends DebugView {
  private val mpp = cmpp / 100;
  
  override def renderParticle(particle:Particle) = {
    val scaledRadius = scaleRadius(particle.shape.radius);
    val scaledCoords = scaleCoordinates(particle.position);
    shapeDrawer.draw(Circle(scaledCoords._1, scaledCoords._2, scaledRadius));
  }
  
  override def renderMotion(oldPosition:Point, particle:Particle) = {
    val oldScaledCoords = scaleCoordinates(oldPosition);
    val newScaledCoords = scaleCoordinates(particle.position);
    val scaledRadius = scaleRadius(particle.shape.radius);
    val clearable = Circle(oldScaledCoords._1, oldScaledCoords._2, scaledRadius);
    val drawable = Circle(newScaledCoords._1, newScaledCoords._2, scaledRadius);
    shapeDrawer.clearAndDraw(clearable, drawable);
  }
  
  private def scaleCoordinates(position:Point) = ((position.x / mpp).toInt, (position.y / mpp).toInt);
  
  private def scaleRadius(radius:Double) = radius / cmpp;
}