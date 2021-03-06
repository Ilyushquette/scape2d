package scape.scape2d.samples

import java.awt.Color
import java.awt.Rectangle
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel
import scape.scape2d.debugger.BodyDebugger
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.TreeBroadPhaseCollisionDetectionDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.ShapeDrawingTreeNodesView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.soft.DiscreteDetectionCollidingSoftBodyDynamics
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
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.motion.collision.detection.broad.TreeDiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.process.simulation.Timescale
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object RectangularBodiesDiagonalCollision {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 27.32, 15.36);
    val treeFactory = () => new QuadTree[Particle](bounds, 4);
    val broadPhaseCollisionDetectionStrategy = new TreeDiscreteBroadPhaseCollisionDetectionStrategy(treeFactory);
    val dynamics = DiscreteDetectionCollidingSoftBodyDynamics(broadPhaseCollisionDetectionStrategy);
    val simulation = new Simulation(dynamics, Timescale(Frequency(120, Second)));
    val simulationThread = new Thread(simulation);
    
    val particlesDrawer = createShapeDrawer();
    val particleDebugger = new ParticleDebugger(new ShapeDrawingParticleTrackingView(particlesDrawer));
    val bodyDebugger = new BodyDebugger(particleDebugger);
    
    val quadTreeNodesDrawer = createShapeDrawer();
    val shapeDrawingQuadTreeNodesView = new ShapeDrawingTreeNodesView(quadTreeNodesDrawer);
    val broadPhaseDetectionDebugger = TreeBroadPhaseCollisionDetectionDebugger(shapeDrawingQuadTreeNodesView);
    broadPhaseDetectionDebugger.trackNodes(broadPhaseCollisionDetectionStrategy);
    
    val body1 = RectangularBodyBuilder()
                .withBodyBuilder(BodyBuilder()
                                 .withParticleFactory(makeMovingParticle)
                                 .withBondFactory(makeBond))
                .withStep(0.15)
                .build(AxisAlignedRectangle(Point(1, 7), 0.75, 0.75));
    val body2 = RectangularBodyBuilder()
                .withBodyBuilder(BodyBuilder()
                                 .withParticleFactory(makeStationaryParticle)
                                 .withBondFactory(makeBond))
                .withStep(0.15)
                .build(AxisAlignedRectangle(Point(13, 6.4), 0.75, 0.75));
    
    initFrame(quadTreeNodesDrawer, particlesDrawer);
    
    bodyDebugger.trackBody(body1);
    bodyDebugger.trackBody(body2);
    dynamics.softBodyDynamics.add(body1);
    dynamics.softBodyDynamics.add(body2);
    simulationThread.start();
  }
  
  private def makeMovingParticle(position:Point) = {
    MovableTrackerProxy.track(ParticleBuilder()
                              .as(Circle(position, 0.05))
                              .withMass(2(Kilogram))
                              .withVelocity(Vector(3, Angle.zero) / Second)
                              .build);
  }
  
  private def makeStationaryParticle(position:Point) = {
    MovableTrackerProxy.track(ParticleBuilder()
                              .as(Circle(position, 0.05))
                              .withMass(2(Kilogram))
                              .build);
  }
  
  private def makeBond(p1:Particle, p2:Particle) = {
    BondBuilder(p1, p2)
    .asElastic(Elastic(LinearStressStrainGraph(60), 99))
    .asPlastic(Plastic(LinearStressStrainGraph(60), 100))
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
  
  private def initFrame(swingShapeDrawers:JPanel*) = {
    val layeredPane = new JLayeredPane;
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize;
    val frame = new JFrame("Scape2D Debugger");
    layeredPane.setPreferredSize(screenSize);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    swingShapeDrawers.foreach(drawer => {
      drawer.setOpaque(false);
      drawer.setBounds(new Rectangle(screenSize));
      layeredPane.add(drawer, swingShapeDrawers.indexOf(drawer), 0);
    });
    frame.add(layeredPane);
    frame.pack();
    frame.setVisible(true);
  }
}