package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.Debugger
import scape.scape2d.debugger.view.ShapeDrawingDebugView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.core.Impulse

object NewtonSecondLaw {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = ParticleBuilder()
      .at(Point.origin)
      .withRadius(5)
      .withMass(2)
      .withVelocity(new Vector2D(1, 45))
      .build;
    
    val trackedMetalParticle = new MovableTrackerProxy(metalParticle);
    val impulse = new Impulse(trackedMetalParticle, new Vector2D(7.68, 45), 2000);
    val impulse2 = new Impulse(trackedMetalParticle, new Vector2D(3.84, 0), 2000);
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK);
    val debugger = new Debugger(new ShapeDrawingDebugView(shapeDrawer, 3));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    debugger.trackParticle(trackedMetalParticle);
    nature.add(trackedMetalParticle);
    nature.add(impulse);
    nature.add(impulse2);
    nature.start;
  }
}