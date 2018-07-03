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
import scape.scape2d.engine.core.matter.shell.RectangularBodyBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.process.simulation.SimulationBuilder
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object RectangularBodyRotation {
  def main(args:Array[String]):Unit = {
    val simulation = SimulationBuilder().build(classOf[Nature]);
    val simulationThread = new Thread(simulation);
    val nature = simulation.process;
    
    val shapeDrawer = createShapeDrawer();
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(shapeDrawer));
    val bodyDebugger = new BodyDebugger(particleDebugger);
    
    val rectangularBody = RectangularBodyBuilder()
                          .withBodyBuilder(BodyBuilder()
                                           .withParticleFactory(makeParticle)
                                           .withBondFactory(makeBond)
                                           .withAngularVelocity(Angle.straight / Second))
                          .withStep(0.15)
                          .build(AxisAlignedRectangle(Point(1, 7), 0.75, 0.75));
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    shapeDrawer.setOpaque(false);
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    bodyDebugger.trackBody(rectangularBody);
    nature.add(rectangularBody);
    simulationThread.start();
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
  
  private def makeParticle(position:Point) = {
    MovableTrackerProxy.track(ParticleBuilder()
                              .as(Circle(position, 0.05))
                              .withMass(2(Kilogram))
                              .withVelocity(Vector(1, Angle.zero) / Second)
                              .build);
  }
  
  private def makeBond(p1:Particle, p2:Particle) = {
    BondBuilder(p1, p2)
    .asElastic(Elastic(LinearStressStrainGraph(10), 99))
    .asPlastic(Plastic(LinearStressStrainGraph(10), 100))
    .withDampingCoefficient(0.1)
    .build;
  }
}