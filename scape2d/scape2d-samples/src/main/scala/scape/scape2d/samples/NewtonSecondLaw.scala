package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.engine.core.matter.Impulse
import scape.scape2d.engine.core.NonRotatableNature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.time._
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.process.simulation.SimulationBuilder

object NewtonSecondLaw {
  def main(args:Array[String]):Unit = {
    val simulation = SimulationBuilder().build(classOf[NonRotatableNature]);
    val simulationThread = new Thread(simulation);
    val nature = simulation.process;
    val metalParticle = ParticleBuilder()
      .as(Circle(Point.origin, 0.05))
      .withMass(2)
      .withVelocity(Vector(1, Angle.bound(45, Degree)) / Second)
      .build;
    
    val trackedMetalParticle = MovableTrackerProxy.track(metalParticle);
    val impulse = new Impulse(trackedMetalParticle, Vector(7.68, Angle.bound(45, Degree)), 2(Second));
    val impulse2 = new Impulse(trackedMetalParticle, Vector(3.84, Angle.zero), 2(Second));
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val converter = ShapeUnitConverter(33);
    val rasterizer = RecursiveRasterizer(
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit().getScreenSize(), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    val debugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    debugger.trackParticle(trackedMetalParticle);
    nature.add(trackedMetalParticle);
    nature.add(impulse);
    nature.add(impulse2);
    simulationThread.start();
  }
}