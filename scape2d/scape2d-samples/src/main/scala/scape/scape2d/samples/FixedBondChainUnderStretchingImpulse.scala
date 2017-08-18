package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Impulse
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.plasticity.Plastic

object FixedBondChainUnderStretchingImpulse {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = ParticleBuilder()
      .as(Circle(Point(10.3, 7), 0.05))
      .withMass(2)
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(10.8, 7), 0.05))
      .withMass(2)
      .build;
    val metalParticle3 = ParticleBuilder()
      .as(Circle(Point(11.3, 7), 0.05))
      .withMass(2)
      .build;
    
    val trackedMetalParticle = new MovableTrackerProxy(metalParticle);
    val trackedMetalParticle2 = new MovableTrackerProxy(metalParticle2);
    val trackedMetalParticle3 = new MovableTrackerProxy(metalParticle3);
    
    val bond = BondBuilder(trackedMetalParticle, trackedMetalParticle2)
      .asElastic(Elastic(LinearStressStrainGraph(10), 99))
      .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
      .withDampingCoefficient(0.1)
      .build;
    val bond2 = BondBuilder(trackedMetalParticle2, trackedMetalParticle3)
      .asElastic(Elastic(LinearStressStrainGraph(10), 99))
      .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
      .withDampingCoefficient(0.1)
      .build;
    
    val impulse = new Impulse(trackedMetalParticle3, new Vector2D(500, 0), 10000);
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK, 0.02);
    shapeDrawer.setOpaque(false);
    val debugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    debugger.trackParticle(trackedMetalParticle);
    debugger.trackParticle(trackedMetalParticle2);
    debugger.trackParticle(trackedMetalParticle3);
    // first particle is not a subject to the laws of nature to be able to simulate fixed point
    nature.add(trackedMetalParticle2);
    nature.add(trackedMetalParticle3);
    nature.add(bond);
    nature.add(bond2);
    nature.add(impulse);
    nature.start;
  }
}