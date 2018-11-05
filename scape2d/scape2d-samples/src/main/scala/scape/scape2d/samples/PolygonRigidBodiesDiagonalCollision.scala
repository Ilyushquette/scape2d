package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import scape.scape2d.debugger.RigidBodyDebugger
import scape.scape2d.debugger.view.ShapeDrawingRigidBodyTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.rigid.DiscreteDetectionCollidingRigidBodyDynamics
import scape.scape2d.engine.core.dynamics.rigid.RigidBodyDynamics
import scape.scape2d.engine.core.matter.RigidBodyBuilder
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.ConvexPolygonBuilder
import scape.scape2d.engine.geom.shape.ConvexShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.collision.detection.BinarySearchDiscreteConvexesContactDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.BruteForceDiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.resolution.RestitutionalActionReactionalCollisionForcesResolver
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object PolygonRigidBodiesDiagonalCollision {
  def main(args:Array[String]) = {
    val dynamics = DiscreteDetectionCollidingRigidBodyDynamics[ConvexShape](
      broadPhaseCollisionDetectionStrategy = BruteForceDiscreteBroadPhaseCollisionDetectionStrategy(),
      contactDetectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy(),
      collisionForcesResolver = RestitutionalActionReactionalCollisionForcesResolver(),
      rigidBodyDynamics = new RigidBodyDynamics()
    );
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    
    val rectangularRigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point(10, 5), 1, 1))
                               .withMass(Mass(10, Kilogram))
                               .build();
    val triangle = ConvexPolygonBuilder(Point(9.25, 10), Point(10.25, 10.5), Point(9.25, 10.5)).build;
    val triangularRigidBody = RigidBodyBuilder(triangle)
                              .withMass(Mass(3, Kilogram))
                              .withVelocity(Vector(1, 270(Degree)) / Second)
                              .build();
    
    val trackedRectangularRigidBody = MovableTrackerProxy.track(rectangularRigidBody);
    val trackedTriangularRigidBody = MovableTrackerProxy.track(triangularRigidBody);
    
    val frame = new JFrame("Scape2D Debugger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    val converter = ShapeUnitConverter(50);
    val buffer = new SwingBuffer(Toolkit.getDefaultToolkit.getScreenSize, true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, RecursiveRasterizer());
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    val debugger = new RigidBodyDebugger(new ShapeDrawingRigidBodyTrackingView(shapeDrawer));
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    debugger.trackRigidBody(trackedRectangularRigidBody);
    debugger.trackRigidBody(trackedTriangularRigidBody);
    dynamics.rigidBodyDynamics.add(trackedRectangularRigidBody);
    dynamics.rigidBodyDynamics.add(trackedTriangularRigidBody);
    simulationThread.start();
  }
}