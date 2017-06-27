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

object TwoParticlesFrontalCollision {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = ParticleBuilder()
      .as(Circle(Point(0, 7), 5))
      .withMass(2)
      .withVelocity(new Vector2D(2, 0))
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(10, 7), 5))
      .withMass(2)
      .withVelocity(new Vector2D(2, 180))
      .build;
    
    val trackedMetalParticle = new MovableTrackerProxy(metalParticle);
    val trackedMetalParticle2 = new MovableTrackerProxy(metalParticle2);
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK);
    val debugger = new Debugger(new ShapeDrawingDebugView(shapeDrawer, 2));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    debugger.trackParticle(trackedMetalParticle);
    debugger.trackParticle(trackedMetalParticle2);
    nature.add(trackedMetalParticle);
    nature.add(trackedMetalParticle2);
    nature.start;
  }
}