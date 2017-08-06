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
  
  def renderParticle(particle:Particle) = {
    shapeDrawer.draw(CustomizedShape(particle.shape, particleColor, true));
    particle.bonds.foreach(bond => shapeDrawer.draw(createShapeFrom(bond)));
  }
  
  def renderMotion(motion:MotionEvent[Particle]) = {
    shapeDrawer.clear(motion.old.shape, true);
    motion.old.bonds.foreach(bond => shapeDrawer.clear(createSegmentFrom(bond), false));
    
    shapeDrawer.draw(CustomizedShape(motion.snapshot.shape, particleColor, true));
    motion.snapshot.bonds.foreach(bond => shapeDrawer.draw(createShapeFrom(bond)));
  }
  
  private def createShapeFrom(bond:Bond) = CustomizedShape(createSegmentFrom(bond), bondColor, false);
  
  private def createSegmentFrom(bond:Bond) = {
    Segment(bond.particles._1.position, bond.particles._2.position);
  }
}