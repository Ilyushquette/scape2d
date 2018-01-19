package scape.scape2d.samples

import java.awt.Color
import java.awt.Rectangle
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.JLayeredPane
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.QuadTreeCollisionDetectorDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.ShapeDrawingQuadTreeNodesView
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.motion.collision.detection._
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
import scape.scape2d.engine.motion.collision.detection.linear.QuadTreeLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.ExtendedSpaceLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.BruteForceLinearMotionCollisionDetector
import scape.scape2d.engine.motion.collision.detection.linear.QuadraticLinearMotionCollisionDetectionStrategy
import scape.scape2d.engine.core.integral.MotionIntegral

object ExtendedSpaceLocatedCollision {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 22.32, 13.36);
    val detectionStrategy = QuadraticLinearMotionCollisionDetectionStrategy[Particle]();
    val quadTreeDetector = new QuadTreeLinearMotionCollisionDetector[Particle](bounds, detectionStrategy);
    val bruteForceDetector = new BruteForceLinearMotionCollisionDetector[Particle](detectionStrategy);
    val collisionDetector = new ExtendedSpaceLinearMotionCollisionDetector(
        coreDetector = quadTreeDetector, 
        regionalDetectorFactory = _ => bruteForceDetector,
        edgeCaseDetectionStrategy = detectionStrategy,
        extension = 100
    );
    val nature = new Nature(motionIntegral = MotionIntegral(LinearMotionIntegral(collisionDetector)));
    val trackedMetalParticles = prepareTrackedMetalParticles();
    
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    val quadTreeNodesDrawer = prepareShapeDrawer();
    val quadTreeNodesView = new ShapeDrawingQuadTreeNodesView(quadTreeNodesDrawer);
    val collisionDetectorDebugger = new QuadTreeCollisionDetectorDebugger(quadTreeNodesView);
    collisionDetectorDebugger.trackNodes(quadTreeDetector);
    
    val particlesDrawer = prepareShapeDrawer();
    val particleTrackingView = new ShapeDrawingParticleTrackingView(particlesDrawer);
    val particleDebugger = new ParticleDebugger(particleTrackingView);
    trackedMetalParticles.foreach(particleDebugger.trackParticle(_));
    
    initFrame(quadTreeNodesDrawer, particlesDrawer);
    
    trackedMetalParticles.foreach(nature.add(_));
    nature.start;
  }
  
  private def prepareTrackedMetalParticles() = {
    val metalParticle = ParticleBuilder()
      .as(Circle(Point(0, 7.03), 0.05))
      .withMass(2)
      .withVelocity(Vector(2, 0))
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(22.32, 6.97), 0.05))
      .withMass(2)
      .withVelocity(Vector(0, 180))
      .build;
    val metalParticle3 = ParticleBuilder()
      .as(Circle(Point(22.52, 6.77), 0.05))
      .withMass(2)
      .withVelocity(Vector(0, 180))
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