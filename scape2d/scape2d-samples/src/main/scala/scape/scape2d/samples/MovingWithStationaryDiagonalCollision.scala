package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import scape.scape2d.debugger.Debugger
import scape.scape2d.debugger.view.ShapeDrawingDebugView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D

object MovingWithStationaryDiagonalCollision {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = ParticleBuilder()
      .at(new Point2D(0, 7.03))
      .withRadius(5)
      .withMass(2)
      .withVelocity(new Vector2D(2, 0))
      .build;
    val metalParticle2 = ParticleBuilder()
      .at(new Point2D(10, 6.97))
      .withRadius(5)
      .withMass(2)
      .withVelocity(new Vector2D(0, 180))
      .build;
    
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
    nature.add(metalParticle);
    nature.add(metalParticle2);
    nature.start;
  }
}