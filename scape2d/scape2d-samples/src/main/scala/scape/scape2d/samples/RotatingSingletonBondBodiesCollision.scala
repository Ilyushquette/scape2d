package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import java.lang.Math.PI
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.structure.HingedSegmentedStructure
import scape.scape2d.debugger.BodyDebugger
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.angle.Radian

object RotatingSingletonBondBodiesCollision {
  def main(args:Array[String]): Unit = {
    val nature = new Nature();
    val singleSegmentStructure1 = HingedSegmentedStructure(Point(13, 7), List(Point(14, 7)));
    val body1 = BodyBuilder()
                .withParticleFactory(makeParticle)
                .withBondFactory(makeBond)
                .withAngularVelocity(2.76(Radian) / Second)
                .build(singleSegmentStructure1.suspension, singleSegmentStructure1);
    
    val singleSegmentStructure2 = HingedSegmentedStructure(Point(11, 7), List(Point(10, 7)));
    val body2 = BodyBuilder()
                .withParticleFactory(makeParticle)
                .withBondFactory(makeBond)
                .withAngularVelocity(UnboundAngle(-3.14, Radian) / Second)
                .build(singleSegmentStructure2.suspension, singleSegmentStructure2);
    
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
    
    bodyDebugger.trackBody(body1);
    bodyDebugger.trackBody(body2);
    nature.add(body1);
    nature.add(body2);
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