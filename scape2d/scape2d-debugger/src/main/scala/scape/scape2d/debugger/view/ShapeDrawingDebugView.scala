package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.Motion

class ShapeDrawingDebugView(val shapeDrawer:ShapeDrawer) extends DebugView {
  val particleColor = 0xff0000; // RED
  
  def renderParticle(particle:Particle) = {
    shapeDrawer.draw(CustomizedShape(particle.shape, particleColor, true));
  }
  
  def renderMotion(motion:Motion[Particle]) = {
    shapeDrawer.clearAndDraw(motion.old.shape, CustomizedShape(motion.snapshot.shape, particleColor, true));
  }
}