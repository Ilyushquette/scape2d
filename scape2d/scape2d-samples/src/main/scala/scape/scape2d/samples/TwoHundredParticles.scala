package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.Debugger
import scape.scape2d.debugger.view.ShapeDrawingDebugView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.geom.shape.Circle

object TwoHundredParticles {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticles = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(i * 0.11, i * 0.11), 0.05))
      .withMass(2)
      .withVelocity(new Vector2D(2, 0))
      .build;
    val metalParticles2 = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(25 - i * 0.11, i * 0.11), 0.05))
      .withMass(2)
      .withVelocity(new Vector2D(2, 180))
      .build;
    
    val trackedMetalParticles = metalParticles.map(new MovableTrackerProxy(_));
    val trackedMetalParticles2 = metalParticles2.map(new MovableTrackerProxy(_));
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK);
    val debugger = new Debugger(new ShapeDrawingDebugView(shapeDrawer, 0.02));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    trackedMetalParticles.foreach(debugger.trackParticle(_));
    trackedMetalParticles2.foreach(debugger.trackParticle(_));
    trackedMetalParticles.foreach(nature.add(_));
    trackedMetalParticles2.foreach(nature.add(_));
    nature.start;
  }
}