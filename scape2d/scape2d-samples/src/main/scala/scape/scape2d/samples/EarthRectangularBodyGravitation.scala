package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JPanel
import scape.scape2d.debugger.BodyDebugger
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovablePhantom
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.soft.DiscreteDetectionCollidingSoftBodyDynamics
import scape.scape2d.engine.core.dynamics.soft.ParticleGravityResolver
import scape.scape2d.engine.core.dynamics.soft.SoftBodyDynamics
import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.core.matter.BondBuilder
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.core.matter.shell.RectangularBodyBuilder
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.deformation.elasticity.Elastic
import scape.scape2d.engine.deformation.plasticity.Plastic
import scape.scape2d.engine.geom.partition.ExpandedTree
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.gravity.PlanetaryNetGravitationalForcesResolver
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.motion.collision.detection.TreePosterioriCollisionDetector
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.process.simulation.Timescale
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object EarthRectangularBodyGravitation {
  def main(args:Array[String]) = {
    val earth = ParticleBuilder()
                .as(Circle(Point(0, -6370995), 6371000))
                .withMass(5.972E24(Kilogram))
                .build;
    
    val particleGravityResolver = createEarthParticleGravityResolver(earth);
    val softBodyDynamics = new SoftBodyDynamics(particleGravityResolver);
    val dynamics = DiscreteDetectionCollidingSoftBodyDynamics(
        collisionDetector = createExpandedTreePosterioriCollisionDetector(),
        softBodyDynamics = softBodyDynamics
    );
    val simulation = new Simulation(dynamics, Timescale(Frequency(240, Second)));
    val simulationThread = new Thread(simulation);
    
    val body = RectangularBodyBuilder()
                .withBodyBuilder(BodyBuilder()
                                 .withParticleFactory(makeParticle)
                                 .withBondFactory(makeBond))
                .withStep(0.15)
                .build(AxisAlignedRectangle(Point(13, 10), 0.75, 0.75));
    
    val particlesDrawer = createShapeDrawer();
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(particlesDrawer));
    val bodyDebugger = new BodyDebugger(particleDebugger);
    
    initFrame(particlesDrawer);
    
    bodyDebugger.trackBody(body);
    softBodyDynamics.add(earth);
    softBodyDynamics.add(body);
    simulationThread.start();
  }
  
  private def createExpandedTreePosterioriCollisionDetector() = {
    val treeFactory = () => new ExpandedTree[MovablePhantom[Particle]](
        coreNode = new QuadTree(AxisAlignedRectangle(Point(0, 0), 27.32, 15.36), 4),
        expansion = 50000000
    );
    TreePosterioriCollisionDetector(treeFactory);
  }
  
  private def createEarthParticleGravityResolver(earth:Particle) = {
    val netGravitationalForcesResolver = PlanetaryNetGravitationalForcesResolver(earth);
    ParticleGravityResolver(netGravitationalForcesResolver);
  }
  
  private def makeParticle(position:Point) = {
    MovableTrackerProxy.track(ParticleBuilder()
                              .as(Circle(position, 0.05))
                              .withMass(2(Kilogram))
                              .build);
  }
  
  private def makeBond(p1:Particle, p2:Particle) = {
    BondBuilder(p1, p2)
    .asElastic(Elastic(LinearStressStrainGraph(120), 99))
    .asPlastic(Plastic(LinearStressStrainGraph(120), 100))
    .withDampingCoefficient(0.1)
    .build;
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
  
  private def initFrame(panel:JPanel) = {
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    panel.setOpaque(false);
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }
}