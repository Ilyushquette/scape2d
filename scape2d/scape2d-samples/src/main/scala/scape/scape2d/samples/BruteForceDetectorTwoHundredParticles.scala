package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.engine.core.NonRotatableNature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.engine.geom.angle.Angle

object BruteForceDetectorTwoHundredParticles {
  def main(args:Array[String]):Unit = {
    val nature = new NonRotatableNature();
    val metalParticles = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(i * 0.11, i * 0.14), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, Angle.zero))
      .build;
    val metalParticles2 = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(25 - i * 0.11, i * 0.14), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, Angle.straight))
      .build;
    
    val trackedMetalParticles = metalParticles.map(MovableTrackerProxy.track(_));
    val trackedMetalParticles2 = metalParticles2.map(MovableTrackerProxy.track(_));
    
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
    val debugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    trackedMetalParticles.foreach(debugger.trackParticle(_));
    trackedMetalParticles2.foreach(debugger.trackParticle(_));
    trackedMetalParticles.foreach(nature.add(_));
    trackedMetalParticles2.foreach(nature.add(_));
    nature.start;
  }
}