package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import scala.actors.Actor

import javax.swing.JFrame
import scape.scape2d.debugger.Debugger
import scape.scape2d.debugger.view.ShapeDrawingDebugView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.ExertForce
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.matter.Particle

object NewtonSecondLaw {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = new Particle(Point2D.origin, 5, 2, new Vector2D(1, 45));
    metalParticle.addMotionListener(_ => {
      nature ! ExertForce(metalParticle, new Vector2D(0.064, 45));
      nature ! ExertForce(metalParticle, new Vector2D(0.034, 0));
    });
    nature.addParticle(metalParticle);
    nature.start;
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK);
    val debugger = new Debugger(new ShapeDrawingDebugView(shapeDrawer, 3));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    debugger.trackParticle(metalParticle);    
  }
}