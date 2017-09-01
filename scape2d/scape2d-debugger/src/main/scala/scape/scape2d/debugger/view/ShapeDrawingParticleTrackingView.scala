package scape.scape2d.debugger.view

import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.graphics.CustomizedShape

class ShapeDrawingParticleTrackingView(val shapeDrawer:ShapeDrawer)
extends ParticleTrackingView {
  val particleColor = 0xFFFF0000; // RED
  val bondColor = 0xFF0000FF; // BLUE
  
  def renderParticle(particle:Particle) = shapeDrawer.draw(CustomizedShape(particle.shape, particleColor));
  
  def clearParticle(particle:Particle) = shapeDrawer.clear(particle.shape);
  
  def renderBond(bond:Bond) = shapeDrawer.draw(createShapeFrom(bond));
  
  def clearBond(bond:Bond) = shapeDrawer.clear(createSegmentFrom(bond));
  
  private def createShapeFrom(bond:Bond) = CustomizedShape(createSegmentFrom(bond), bondColor);
  
  private def createSegmentFrom(bond:Bond) = {
    Segment(bond.particles._1.position, bond.particles._2.position);
  }
}