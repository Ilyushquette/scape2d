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
import scape.scape2d.engine.core.dynamics.soft.ContinuousDetectionCollidingSoftBodyDynamics
import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.geom.structure.HingedSegmentedStructure
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object RotatingSingletonBondBodyWithStationaryCollision {
  def main(args:Array[String]): Unit = {
    val dynamics = ContinuousDetectionCollidingSoftBodyDynamics();
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    val singleSegmentStructure = HingedSegmentedStructure(Point(13, 7), List(Point(13.66, 7.73)));
    val body = BodyBuilder()
               .withParticleFactory(makeParticle)
               .withBondFactory(makeBond)
               .withAngularVelocity(157(Degree) / Second)
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
    dynamics.softBodyDynamics.add(body);
    dynamics.softBodyDynamics.add(stationaryParticle);
    simulationThread.start();
  }
  
  private def makeParticle(position:Point) = {
    MovableTrackerProxy.track(ParticleBuilder()
                              .as(Circle(position, 0.13))
                              .withMass(Mass(2, Kilogram))
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