package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.engine.core.NonRotatableNature
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.debugger.BondDebugger
import java.lang.Math.PI

object BondPostDiagonalCollisionCompression {
  def main(args:Array[String]):Unit = {
    val nature = new NonRotatableNature();
    val metalParticle = ParticleBuilder()
      .as(Circle(Point(10.3, 7), 0.05))
      .withMass(2)
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(10.8, 7), 0.05))
      .withMass(2)
      .build;
    val metalParticle3 = ParticleBuilder()
      .as(Circle(Point(14.8, 7.025), 0.05))
      .withMass(2)
      .withVelocity(Vector(3, PI))
      .build;
    
    val trackedMetalParticle = MovableTrackerProxy.track(metalParticle);
    val trackedMetalParticle2 = MovableTrackerProxy.track(metalParticle2);
    val trackedMetalParticle3 = MovableTrackerProxy.track(metalParticle3);
    
    val bond = BondBuilder(trackedMetalParticle, trackedMetalParticle2)
      .asElastic(Elastic(LinearStressStrainGraph(10), 99))
      .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
      .withDampingCoefficient(0.1)
      .build;
    
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
    val bondDebugger = new BondDebugger(particleDebugger);
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    bondDebugger.trackBond(bond);
    particleDebugger.trackParticle(trackedMetalParticle3);
    nature.add(bond);
    nature.add(trackedMetalParticle3);
    nature.start;
  }
}