package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.engine.core.NonRotatableNature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.debugger.TreeLinearMotionCollisionDetectorDebugger
import scape.scape2d.debugger.view.ShapeDrawingTreeNodesView
import javax.swing.JLayeredPane
import java.awt.BorderLayout
import java.awt.Rectangle
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import javax.swing.JPanel
import scape.scape2d.engine.core.integral.LinearMotionIntegral
import scape.scape2d.engine.motion.collision.detection.linear.TreeLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.core.integral.MotionIntegral
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.motion.linear.LinearSweepFormingMovable
import java.lang.Math.PI

object QuadTreeDetectorTwoHundredParticles {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 27.32, 15.36);
    val detectionStrategy = QuadraticLinearMotionCollisionDetectionStrategy[Particle]();
    val treeFactory = () => new QuadTree[LinearSweepFormingMovable[Particle]](bounds, 4);
    val collisionDetector = TreeLinearMotionCollisionDetector[Particle](treeFactory, detectionStrategy);
    val nature = new NonRotatableNature(linearMotionIntegral = LinearMotionIntegral(collisionDetector));
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
    
    trackedMetalParticles.foreach(nature.add(_));
    nature.start;
  }
  
  private def prepareTrackedMetalParticles() = {
    val metalParticles = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(i * 0.11, i * 0.14), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, 0))
      .build;
    val metalParticles2 = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(25 - i * 0.11, i * 0.14), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, PI))
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