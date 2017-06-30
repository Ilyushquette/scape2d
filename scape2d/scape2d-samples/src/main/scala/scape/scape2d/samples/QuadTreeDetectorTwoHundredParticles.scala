package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import scape.scape2d.debugger.ParticleDebugger
import scape.scape2d.debugger.view.ShapeDrawingParticleTrackingView
import scape.scape2d.debugger.view.swing.SwingShapeDrawer
import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.core.matter.ParticleBuilder
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.MovableTrackerProxy
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.debugger.QuadTreeCollisionDetectorDebugger
import scape.scape2d.debugger.view.ShapeDrawingQuadTreeNodesView
import javax.swing.JLayeredPane
import java.awt.BorderLayout
import java.awt.Rectangle

object QuadTreeDetectorTwoHundredParticles {
  def main(args:Array[String]):Unit = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 27.32, 15.36);
    val collisionDetector = new QuadTreeBasedCollisionDetector[Particle](bounds, detectWithDiscriminant);
    val nature = new Nature(collisionDetector, 60);
    val trackedMetalParticles = prepareTrackedMetalParticles();
    
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    val quadTreeNodesDrawer = new SwingShapeDrawer(screenSize, Color.BLACK, 0.02);
    val quadTreeNodesView = new ShapeDrawingQuadTreeNodesView(quadTreeNodesDrawer);
    val collisionDetectorDebugger = new QuadTreeCollisionDetectorDebugger(quadTreeNodesView);
    collisionDetectorDebugger.trackNodes(collisionDetector);
    
    val particlesDrawer = new SwingShapeDrawer(screenSize, Color.BLACK, 0.02);
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
      .withVelocity(new Vector2D(2, 0))
      .build;
    val metalParticles2 = for(i <- 0 to 100) yield ParticleBuilder()
      .as(Circle(Point(25 - i * 0.11, i * 0.14), 0.05))
      .withMass(2)
      .withVelocity(new Vector2D(2, 180))
      .build;
    
    val trackedMetalParticles = metalParticles.map(new MovableTrackerProxy(_));
    val trackedMetalParticles2 = metalParticles2.map(new MovableTrackerProxy(_));
    
    trackedMetalParticles ++ trackedMetalParticles2;
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