package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.motion.MotionEvent
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.geom.shape.Point

class ShapeDrawingParticleTrackingView(val shapeDrawer:ShapeDrawer)
extends ParticleTrackingView {
  val particleColor = 0xff0000; // RED
  val bondColor = 0x0000ff; // BLUE
  
  def renderParticle(particle:Particle) = shapeDrawer.draw(CustomizedShape(particle.shape, particleColor, true));
  
  def clearParticle(particle:Particle) = shapeDrawer.clear(particle.shape, true);
  
  def renderBond(bond:Bond) = shapeDrawer.draw(createShapeFrom(bond));
  
  def clearBond(bond:Bond) = shapeDrawer.clear(createSegmentFrom(bond), false);
  
  private def createShapeFrom(bond:Bond) = CustomizedShape(createSegmentFrom(bond), bondColor, false);
  
  private def createSegmentFrom(bond:Bond) = {
    Segment(bond.particles._1.position, bond.particles._2.position);
  }
}