package scape.scape2d.samples

import java.awt.Color
import java.awt.Rectangle
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.TreeLinearMotionCollisionDetectorDebugger
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
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.linear.TreeLinearMotionCollisionDetector
import scape.scape2d.engine.motion.linear.LinearSweepFormingMovable
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

object QuadTreeDetectorTwoHundredParticles {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 27.32, 15.36);
    val detectionStrategy = QuadraticLinearMotionCollisionDetectionStrategy[Particle]();
    val treeFactory = () => new QuadTree[LinearSweepFormingMovable[Particle]](bounds, 4);
    val collisionDetector = TreeLinearMotionCollisionDetector[Particle](treeFactory, detectionStrategy);
    val dynamics = ContinuousDetectionCollidingLinearSoftBodyDynamics(collisionDetector);
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    val trackedMetalParticles = prepareTrackedMetalParticles();
    
    val quadTreeNodesDrawer = prepareShapeDrawer();
    val quadTreeNodesView = new ShapeDrawingTreeNodesView(quadTreeNodesDrawer);
    val collisionDetectorDebugger = new TreeLinearMotionCollisionDetectorDebugger(quadTreeNodesView);
    collisionDetectorDebugger.trackNodes(collisionDetector);
    
    val particlesDrawer = prepareShapeDrawer();
    val particleTrackingView = new ShapeDrawingParticleTrackingView(particlesDrawer);
    val particleDebugger = new ParticleDebugger(particleTrackingView);
    trackedMetalParticles.foreach(particleDebugger.trackParticle(_));
    
    initFrame(quadTreeNodesDrawer, particlesDrawer);
    
    trackedMetalParticles.foreach(dynamics.linearSoftBodyDynamics.add(_));
    simulationThread.start();
  }
  
  private def prepareTrackedMetalParticles() = {
    val metalParticles = for(i <- 0 to 100) yield ParticleBuilder()
                                                  .as(Circle(Point(i * 0.11, i * 0.14), 0.05))
                                                  .withMass(2(Kilogram))
                                                  .withVelocity(Vector(2, Angle.zero) / Second)
                                                  .build;
    val metalParticles2 = for(i <- 0 to 100) yield ParticleBuilder()
                                                   .as(Circle(Point(25 - i * 0.11, i * 0.14), 0.05))
                                                   .withMass(2(Kilogram))
                                                   .withVelocity(Vector(2, Angle.straight) / Second)
                                                   .build;
    
    val trackedMetalParticles = metalParticles.map(MovableTrackerProxy.track(_));
    val trackedMetalParticles2 = metalParticles2.map(MovableTrackerProxy.track(_));
    
    trackedMetalParticles ++ trackedMetalParticles2;
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