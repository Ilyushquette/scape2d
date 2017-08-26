package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import java.util.Timer
import java.util.TimerTask
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.Impulse
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.motion.MovableTrackerProxy.autoEnhance
import java.awt.Dimension
import scape.scape2d.debugger.view.ShapeDrawingGraphView
import scape.scape2d.engine.deformation.BondStructureTrackerProxy
import scape.scape2d.debugger.BondDebugger
import javax.swing.JPanel
import javax.swing.border.EmptyBorder
import scape.scape2d.debugger.view.ParticleTrackingView

object FixedBondHardeningUnderStretchingImpulse {
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
    
    val trackedMetalParticle = new MovableTrackerProxy(metalParticle);
    val trackedMetalParticle2 = new MovableTrackerProxy(metalParticle2);
    
    val bond = BondBuilder(trackedMetalParticle, trackedMetalParticle2)
      .asElastic(Elastic(LinearStressStrainGraph(10), 0.3))
      .asPlastic(Plastic(LinearStressStrainGraph(10), 1))
      .withDampingCoefficient(0.1)
      .build;
    
    val structureTrackedBond = new BondStructureTrackerProxy(bond);
    
    val particlesDebugger = initParticlesDebugger();
    particlesDebugger.trackParticle(trackedMetalParticle);
    particlesDebugger.trackParticle(trackedMetalParticle2);
    
    val bondsDebugger = initBondsDebugger(particlesDebugger.particleTrackingView);
    bondsDebugger.trackStructureEvolution(structureTrackedBond);
    
    // first particle is not a subject to the laws of nature to be able to simulate fixed point
    nature.add(trackedMetalParticle2);
    nature.add(structureTrackedBond);
    nature.start;
    
    val timer = new Timer;
    timer.schedule(new TimerTask {
      def run() = nature.add(new Impulse(trackedMetalParticle2, new Vector2D(450, 0), 2500));
    }, 3000);
  }
  
  private def initParticlesDebugger() = {
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val shapeDrawer = new SwingShapeDrawer(Toolkit.getDefaultToolkit().getScreenSize(), Color.BLACK, 0.02);
    shapeDrawer.setOpaque(false);
    val debugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    debugger;
  }
  
  private def initBondsDebugger(particleTrackingView:ParticleTrackingView) = {
    val graphViewFactory = () => {
      val frame = new JFrame("Scape2D Stress(Y) vs Strain(X) graph");
      val contentPanel = new JPanel;
      contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      frame.setContentPane(contentPanel);
      frame.getContentPane.setBackground(Color.BLACK);
      val shapeDrawer = new SwingShapeDrawer(new Dimension(300, 250), Color.BLACK, 0.05);
      shapeDrawer.setOpaque(false);
      frame.add(shapeDrawer);
      frame.pack();
      frame.setVisible(true);
      new ShapeDrawingGraphView(shapeDrawer);
    }
    new BondDebugger(particleTrackingView, graphViewFactory);
  }
}