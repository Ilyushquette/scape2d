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

object TwoParticlesFrontalCollision {
  def main(args:Array[String]):Unit = {
    val nature = new NonRotatableNature();
    val metalParticle = ParticleBuilder()
      .as(Circle(Point(0, 7), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, Angle.zero))
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(10, 7), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, Angle.straight))
      .build;
    
    val trackedMetalParticle = MovableTrackerProxy.track(metalParticle);
    val trackedMetalParticle2 = MovableTrackerProxy.track(metalParticle2);
    
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
    
    debugger.trackParticle(trackedMetalParticle);
    debugger.trackParticle(trackedMetalParticle2);
    nature.add(trackedMetalParticle);
    nature.add(trackedMetalParticle2);
    nature.start;
  }
}