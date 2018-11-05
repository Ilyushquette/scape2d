package scape.scape2d.samples

import java.awt.Color
import java.awt.Rectangle
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.ShapeDrawingTreeNodesView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.soft.linear.ContinuousDetectionCollidingLinearSoftBodyDynamics
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.partition.ExpandedTree
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.linear.LinearSweepFormingMovable
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.engine.motion.MotionBounds
import scape.scape2d.engine.motion.collision.detection.broad.TreeContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.debugger.TreeBroadPhaseCollisionDetectionDebugger

object ExtendedSpaceLocatedCollision {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 22.32, 13.36);
    val treeFactory = () => new ExpandedTree(
        coreNode = new QuadTree[MotionBounds[Particle]](bounds, 4),
        expansion = 5
    );
    val broadPhaseDetectionStrategy = new TreeContinuousBroadPhaseCollisionDetectionStrategy(treeFactory);
    val dynamics = ContinuousDetectionCollidingLinearSoftBodyDynamics(broadPhaseDetectionStrategy);
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    val trackedMetalParticles = prepareTrackedMetalParticles();
    
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    val quadTreeNodesDrawer = prepareShapeDrawer();
    val quadTreeNodesView = new ShapeDrawingTreeNodesView(quadTreeNodesDrawer);
    val broadPhaseDetectionDebugger = TreeBroadPhaseCollisionDetectionDebugger(quadTreeNodesView);
    broadPhaseDetectionDebugger.trackNodes(broadPhaseDetectionStrategy);
    
    val particlesDrawer = prepareShapeDrawer();
    val particleTrackingView = new ShapeDrawingParticleTrackingView(particlesDrawer);
    val particleDebugger = new ParticleDebugger(particleTrackingView);
    trackedMetalParticles.foreach(particleDebugger.trackParticle(_));
    
    initFrame(quadTreeNodesDrawer, particlesDrawer);
    
    trackedMetalParticles.foreach(dynamics.linearSoftBodyDynamics.add(_));
    simulationThread.start();
  }
  
  private def prepareTrackedMetalParticles() = {
    val metalParticle = ParticleBuilder()
                        .as(Circle(Point(0, 7.03), 0.05))
                        .withMass(2(Kilogram))
                        .withVelocity(Vector(2, Angle.zero) / Second)
                        .build;
    val metalParticle2 = ParticleBuilder()
                         .as(Circle(Point(22.32, 6.97), 0.05))
                         .withMass(2(Kilogram))
                         .withVelocity(Velocity.zero)
                         .build;
    val metalParticle3 = ParticleBuilder()
                         .as(Circle(Point(22.52, 6.77), 0.05))
                         .withMass(2(Kilogram))
                         .withVelocity(Velocity.zero)
                         .build;
    
    Array(MovableTrackerProxy.track(metalParticle),
          MovableTrackerProxy.track(metalParticle2),
          MovableTrackerProxy.track(metalParticle3));
  }
  
  private def prepareShapeDrawer() = {
    val converter = ShapeUnitConverter(50);
    val rasterizer = RecursiveRasterizer(
        segmentRasterizer = CachingRasterizers.enhanceSegmentRasterizer(NaiveSegmentRasterizer()),
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit().getScreenSize(), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    shapeDrawer;
  }
  
  private def initFrame(panels:JPanel*) = {
    val layeredPane = new JLayeredPane;
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize;
    val frame = new JFrame("Scape2D Debugger");
    layeredPane.setPreferredSize(screenSize);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    panels.foreach(drawer => {
      drawer.setOpaque(false);
      drawer.setBounds(new Rectangle(screenSize));
      layeredPane.add(drawer, panels.indexOf(drawer), 0);
    });
    frame.add(layeredPane);
    frame.pack();
    frame.setVisible(true);
  }
}