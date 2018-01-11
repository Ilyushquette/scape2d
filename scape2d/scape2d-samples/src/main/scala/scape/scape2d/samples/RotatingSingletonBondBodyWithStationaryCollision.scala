package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import scape.scape2d.debugger.BodyDebugger
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.geom.structure.HingedSegmentedStructure
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object RotatingSingletonBondBodyWithStationaryCollision {
  def main(args:Array[String]): Unit = {
    val nature = new Nature();
    val singleSegmentStructure = HingedSegmentedStructure(Point(13, 7), List(Point(13.66, 7.73)));
    val body = BodyBuilder()
               .withParticleFactory(makeParticle)
               .withBondFactory(makeBond)
               .withAngularVelocity(2.74017)
               .build(singleSegmentStructure.suspension, singleSegmentStructure);
    val stationaryParticle = makeParticle(Point(12.5, 6));
    
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
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    val bodyDebugger = new BodyDebugger(particleDebugger);
    
    bodyDebugger.trackBody(body);
    particleDebugger.trackParticle(stationaryParticle);
    nature.add(body);
    nature.add(stationaryParticle);
    nature.start();
  }
  
  private def makeParticle(position:Point) = MovableTrackerProxy.track(ParticleBuilder()
                                                                       .as(Circle(position, 0.13))
                                                                       .withMass(2)
                                                                       .build);
  
  private def makeBond(p1:Particle, p2:Particle) = BondBuilder(p1, p2)
                                                   .asElastic(Elastic(LinearStressStrainGraph(10), 99))
                                                   .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
                                                   .withDampingCoefficient(0.1)
                                                   .build;
}