package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.soft.linear.ContinuousDetectionCollidingLinearSoftBodyDynamics
import scape.scape2d.engine.core.matter.BondBuilder
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
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.doubleToTime
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object FixedBondChainUnderStretchingImpulse {
  def main(args:Array[String]):Unit = {
    val dynamics = ContinuousDetectionCollidingLinearSoftBodyDynamics();
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
    val metalParticle3 = ParticleBuilder()
                         .as(Circle(Point(11.3, 7), 0.05))
                         .withMass(Mass(2, Kilogram))
                         .build;
    
    val trackedMetalParticle = MovableTrackerProxy.track(metalParticle);
    val trackedMetalParticle2 = MovableTrackerProxy.track(metalParticle2);
    val trackedMetalParticle3 = MovableTrackerProxy.track(metalParticle3);
    
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
    
    val impulse = new Impulse(trackedMetalParticle3, Vector(500, Angle.zero), 10(Second));
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val converter = ShapeUnitConverter(50);
    val rasterizer = RecursiveRasterizer(
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit().getScreenSize(), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    particleDebugger.trackParticle(trackedMetalParticle);
    particleDebugger.trackParticle(trackedMetalParticle2);
    particleDebugger.trackParticle(trackedMetalParticle3);
    // first particle is not a subject to the laws of nature to be able to emulate fixed point
    dynamics.linearSoftBodyDynamics.add(trackedMetalParticle2);
    dynamics.linearSoftBodyDynamics.add(trackedMetalParticle3);
    dynamics.linearSoftBodyDynamics.add(impulse);
    simulationThread.start();
  }
}