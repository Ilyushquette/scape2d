package scape.scape2d.samples

import java.awt.Color
import java.awt.Rectangle
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel
import scape.scape2d.debugger.RigidBodyDebugger
import scape.scape2d.debugger.TreeBroadPhaseCollisionDetectionDebugger
import scape.scape2d.debugger.view.ShapeDrawingRigidBodyTrackingView
import scape.scape2d.debugger.view.ShapeDrawingTreeNodesView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.rigid.DiscreteDetectionCollidingRigidBodyDynamics
import scape.scape2d.engine.core.dynamics.rigid.RigidBodyDynamics
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.core.matter.RigidBodyBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.ConvexPolygonBuilder
import scape.scape2d.engine.geom.shape.ConvexShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.collision.detection.BinarySearchDiscreteConvexesContactDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.TreeDiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.resolution.RestitutionalActionReactionalCollisionForcesResolver
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object CirclePolygonRigidBodiesDiagonalCollision {
  def main(args:Array[String]) = {
    val bounds = AxisAlignedRectangle(Point(0, 0), 27.32, 15.36);
    val treeFactory = () => new QuadTree[RigidBody[_ <: ConvexShape]](bounds, 3);
    val broadPhaseCollisionDetectionStrategy = new TreeDiscreteBroadPhaseCollisionDetectionStrategy(treeFactory);
    val dynamics = DiscreteDetectionCollidingRigidBodyDynamics[ConvexShape](
      broadPhaseCollisionDetectionStrategy = broadPhaseCollisionDetectionStrategy,
      contactDetectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy(),
      collisionForcesResolver = RestitutionalActionReactionalCollisionForcesResolver(),
      rigidBodyDynamics = new RigidBodyDynamics()
    );
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    
    val circularRigidBody = RigidBodyBuilder(Circle(Point(7, 2), 0.5))
                            .withMass(Mass(7, Kilogram))
                            .withVelocity(Vector(1.5, 45(Degree)) / Second)
                            .build();
    val rectangularRigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point(10, 5), 1, 1))
                               .withMass(Mass(10, Kilogram))
                               .build();
    
    val circularRigidBody2 = RigidBodyBuilder(Circle(Point(14.5, 10), 0.5))
                             .withMass(Mass(10, Kilogram))
                             .build();
    val triangle = ConvexPolygonBuilder(Point(1, 12), Point(2, 12.5), Point(1, 12.5)).build;
    val triangularRigidBody = RigidBodyBuilder(triangle)
                              .withMass(Mass(3, Kilogram))
                              .build();
    
    val trackedCircularRigidBody = MovableTrackerProxy.track(circularRigidBody);
    val trackedRectangularRigidBody = MovableTrackerProxy.track(rectangularRigidBody);
    val trackedCircularRigidBody2 = MovableTrackerProxy.track(circularRigidBody2);
    val trackedTriangularRigidBody = MovableTrackerProxy.track(triangularRigidBody);
    
    val rigidBodiesDrawer = createShapeDrawer();
    val rigidBodyDebugger = new RigidBodyDebugger(new ShapeDrawingRigidBodyTrackingView(rigidBodiesDrawer));
    
    val quadTreeNodesDrawer = createShapeDrawer();
    val shapeDrawingQuadTreeNodesView = new ShapeDrawingTreeNodesView(quadTreeNodesDrawer);
    val quadTreeBroadPhaseFilterDebugger = TreeBroadPhaseCollisionDetectionDebugger(shapeDrawingQuadTreeNodesView);
    quadTreeBroadPhaseFilterDebugger.trackNodes(broadPhaseCollisionDetectionStrategy);
    
    initFrame(quadTreeNodesDrawer, rigidBodiesDrawer);
    
    rigidBodyDebugger.trackRigidBody(trackedCircularRigidBody);
    rigidBodyDebugger.trackRigidBody(trackedRectangularRigidBody);
    rigidBodyDebugger.trackRigidBody(trackedCircularRigidBody2);
    rigidBodyDebugger.trackRigidBody(trackedTriangularRigidBody);
    dynamics.rigidBodyDynamics.add(trackedCircularRigidBody);
    dynamics.rigidBodyDynamics.add(trackedRectangularRigidBody);
    dynamics.rigidBodyDynamics.add(trackedCircularRigidBody2);
    dynamics.rigidBodyDynamics.add(trackedTriangularRigidBody);
    simulationThread.start();
  }
  
  private def createShapeDrawer() = {
    val converter = ShapeUnitConverter(50);
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit().getScreenSize(), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, RecursiveRasterizer());
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