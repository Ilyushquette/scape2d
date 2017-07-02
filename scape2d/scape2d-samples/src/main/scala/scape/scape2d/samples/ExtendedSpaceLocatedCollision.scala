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
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.motion.collision.detection._

object ExtendedSpaceLocatedCollision {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 22.32, 13.36);
    val coreDetector = new QuadTreeBasedCollisionDetector[Particle](bounds, detectWithDiscriminant);
    val bucketDetector = new BruteForceBasedCollisionDetector[Particle](detectWithDiscriminant);
    val collisionDetector = new ExtendedSpaceCollisionDetector(coreDetector, _ => bucketDetector,
                                                               detectWithDiscriminant, 100);
    val nature = new Nature(collisionDetector, 60);
    val trackedMetalParticles = prepareTrackedMetalParticles();
    
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    val quadTreeNodesDrawer = new SwingShapeDrawer(screenSize, Color.BLACK, 0.02);
    val quadTreeNodesView = new ShapeDrawingQuadTreeNodesView(quadTreeNodesDrawer);
    val collisionDetectorDebugger = new QuadTreeCollisionDetectorDebugger(quadTreeNodesView);
    collisionDetectorDebugger.trackNodes(coreDetector);
    
    val particlesDrawer = new SwingShapeDrawer(screenSize, Color.BLACK, 0.02);
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
      .withVelocity(new Vector2D(2, 0))
      .build;
    val metalParticle2 = ParticleBuilder()
      .as(Circle(Point(22.32, 6.97), 0.05))
      .withMass(2)
      .withVelocity(new Vector2D(0, 180))
      .build;
    val metalParticle3 = ParticleBuilder()
      .as(Circle(Point(22.52, 6.77), 0.05))
      .withMass(2)
      .withVelocity(new Vector2D(0, 180))
      .build;
    
    Array(new MovableTrackerProxy(metalParticle),
          new MovableTrackerProxy(metalParticle2),
          new MovableTrackerProxy(metalParticle3));
  }
  
  private def initFrame(swingShapeDrawers:SwingShapeDrawer*) = {
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