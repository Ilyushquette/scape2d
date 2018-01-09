package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.core.matter.shell.RectangularBodyBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.motion.collision.detection.QuadTreeBasedCollisionDetector
import scape.scape2d.engine.motion.collision.detection.detectWithDiscriminant
import scape.scape2d.engine.time._
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.engine.core.matter.TorqueImpulse
import scape.scape2d.debugger.BodyDebugger
import scape.scape2d.engine.core.integral.LinearMotionIntegral

object RectangularBodyRotationUnderTorqueImpulse {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point.origin, 27.32, 15.36);
    val collisionDetector = new QuadTreeBasedCollisionDetector[Particle](bounds, detectWithDiscriminant);
    val nature = new Nature(linearMotionIntegral = LinearMotionIntegral(collisionDetector));
    
    val shapeDrawer = createShapeDrawer();
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    val bodyDebugger = new BodyDebugger(particleDebugger);
    
    val rectangularBody = RectangularBodyBuilder()
                          .withBodyBuilder(BodyBuilder()
                                           .withParticleFactory(makeParticle)
                                           .withBondFactory(makeBond))
                          .withStep(0.15)
                          .build(AxisAlignedRectangle(Point(1, 7), 0.75, 0.75));
    val particleNearestToOrigin = rectangularBody.movables.minBy(_.position distanceTo Point.origin);
    val torqueImpulse = new TorqueImpulse(particleNearestToOrigin, 120, 1(Minute));
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    shapeDrawer.setOpaque(false);
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    bodyDebugger.trackBody(rectangularBody);
    nature.add(rectangularBody);
    nature.add(torqueImpulse);
    nature.start();
  }
  
  private def createShapeDrawer() = {
    val converter = ShapeUnitConverter(50);
    val rasterizer = RecursiveRasterizer(
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit().getScreenSize(), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
  }
  
  private def makeParticle(position:Point) = MovableTrackerProxy.track(ParticleBuilder()
                                                                       .as(Circle(position, 0.05))
                                                                       .withMass(2)
                                                                       .build);
  
  private def makeBond(p1:Particle, p2:Particle) = BondBuilder(p1, p2)
                                                   .asElastic(Elastic(LinearStressStrainGraph(10), 99))
                                                   .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
                                                   .withDampingCoefficient(0.1)
                                                   .build;
}