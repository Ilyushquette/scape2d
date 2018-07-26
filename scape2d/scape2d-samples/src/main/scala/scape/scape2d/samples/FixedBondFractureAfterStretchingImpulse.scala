package scape.scape2d.samples

import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.border.EmptyBorder
import scape.scape2d.debugger.BondDebugger
import scape.scape2d.debugger.BondStructureDebugger
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ParticleTrackingView
import scape.scape2d.debugger.view.ShapeDrawingGraphView
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.soft.linear.ContinuousDetectionCollidingLinearSoftBodyDynamics
import scape.scape2d.engine.core.dynamics.soft.linear.LinearSoftBodyDynamics
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.BondStructureTrackerProxy
import scape.scape2d.engine.core.matter.Impulse
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.process.ProcessOperationsDeferringProxy
import scape.scape2d.engine.process.ProcessOperationsDeferringProxy.autoEnhance
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.doubleToTime
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object FixedBondFractureAfterStretchingImpulse {
  def main(args:Array[String]):Unit = {
    val dynamics = ContinuousDetectionCollidingLinearSoftBodyDynamics(
        linearSoftBodyDynamics = new ProcessOperationsDeferringProxy(new LinearSoftBodyDynamics())
    );
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    val metalParticle = ParticleBuilder()
                        .as(Circle(Point(10.3, 7), 0.05))
                        .withMass(Mass(2, Kilogram))
                        .build;
    val metalParticle2 = ParticleBuilder()
                         .as(Circle(Point(10.8, 7), 0.05))
                         .withMass(Mass(2, Kilogram))
                         .build;
    
    val trackedMetalParticle = MovableTrackerProxy.track(metalParticle);
    val trackedMetalParticle2 = MovableTrackerProxy.track(metalParticle2);
    
    val bond = BondBuilder(trackedMetalParticle, trackedMetalParticle2)
               .asElastic(Elastic(LinearStressStrainGraph(10), 0.3))
               .asPlastic(Plastic(LinearStressStrainGraph(10), 1))
               .withDampingCoefficient(0.1)
               .build;
    
    val structureTrackedBond = BondStructureTrackerProxy.track(bond);
    
    val particleDebugger = initParticleDebugger();
    val bondDebugger = new BondDebugger(particleDebugger);
    val bondStructureDebugger = initBondStructureDebugger(particleDebugger.particleTrackingView);
    
    bondDebugger.trackBond(bond);
    bondStructureDebugger.trackStructureEvolution(structureTrackedBond);
    // first particle is not a subject to the laws of nature to be able to emulate fixed point
    dynamics.linearSoftBodyDynamics.add(trackedMetalParticle2);
    simulationThread.start();
    
    Thread.sleep(3000);
    dynamics.linearSoftBodyDynamics.add(new Impulse(trackedMetalParticle2, Vector(85, Angle.zero), 50(Millisecond)));
  }
  
  private def initParticleDebugger() = {
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val converter = ShapeUnitConverter(50);
    val rasterizer = RecursiveRasterizer(
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit.getScreenSize, true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    val debugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    debugger;
  }
  
  private def initBondStructureDebugger(particleTrackingView:ParticleTrackingView) = {
    val graphViewFactory = () => {
      val frame = new JFrame("Scape2D Stress(Y) vs Strain(X) graph");
      val contentPanel = new JPanel;
      contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      frame.setContentPane(contentPanel);
      frame.getContentPane.setBackground(Color.BLACK);
      val converter = ShapeUnitConverter(20);
      val buffer = new SwingBuffer(new Dimension(300, 250), true);
      val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, RecursiveRasterizer());
      val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
      shapeDrawer.setOpaque(false);
      frame.add(shapeDrawer);
      frame.pack();
      frame.setVisible(true);
      new ShapeDrawingGraphView(shapeDrawer);
    }
    new BondStructureDebugger(particleTrackingView, graphViewFactory);
  }
}