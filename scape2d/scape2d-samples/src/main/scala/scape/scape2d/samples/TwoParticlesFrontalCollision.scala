package scape.scape2d.samples

import java.awt.Color
import javax.swing.JFrame
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.matter.Particle
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.debugger.Debugger
import scape.scape2d.debugger.view.ShapeDrawingDebugView
import java.awt.Toolkit

object TwoParticlesFrontalCollision {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = new Particle(new Point2D(0, 7), 5, 2, new Vector2D(2, 0));
    val metalParticle2 = new Particle(new Point2D(10, 7), 5, 2, new Vector2D(2, 180));
    nature.add(metalParticle);
    nature.add(metalParticle2);
    nature.start;
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK);
    val debugger = new Debugger(new ShapeDrawingDebugView(shapeDrawer, 2));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    debugger.trackParticle(metalParticle);
    debugger.trackParticle(metalParticle2);
  }
}