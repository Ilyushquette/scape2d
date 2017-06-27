package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.Motion

class ShapeDrawingDebugView(val shapeDrawer:ShapeDrawer, val mpp:Double) extends DebugView {
  override def renderParticle(particle:Particle) = {
    val scaledRadius = scaleRadius(particle.shape.radius);
    val scaledCoords = scaleCoordinates(particle.position);
    shapeDrawer.draw(Circle(scaledCoords._1, scaledCoords._2, scaledRadius));
  }
  
  override def renderMotion(motion:Motion[Particle]) = {
    val oldScaledCoords = scaleCoordinates(motion.old.position);
    val newScaledCoords = scaleCoordinates(motion.snapshot.position);
    val scaledRadius = scaleRadius(motion.snapshot.shape.radius);
    val clearable = Circle(oldScaledCoords._1, oldScaledCoords._2, scaledRadius);
    val drawable = Circle(newScaledCoords._1, newScaledCoords._2, scaledRadius);
    shapeDrawer.clearAndDraw(clearable, drawable);
  }
  
  private def scaleCoordinates(position:Point) = ((position.x / mpp).toInt, (position.y / mpp).toInt);
  
  private def scaleRadius(radius:Double) = radius / mpp;
}