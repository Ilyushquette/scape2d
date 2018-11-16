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
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.ConvexShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.gravity.ConstantAccelerationNetGravitationalForcesResolver
import scape.scape2d.engine.gravity.IgnoredMatterNetGravitationalForcesResolver
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.motion.collision.detection.BinarySearchDiscreteConvexesContactDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.BruteForceDiscreteBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.resolution.RestitutionalActionReactionalCollisionForcesResolver
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.process.simulation.Timescale
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer

object PyramidBlockSystemFriction {
  def main(args:Array[String]) = {
    val floor = RigidBodyBuilder(AxisAlignedRectangle(Point(1, 1), 24, 3))
                .withMass(5.972E24(Kilogram))
                .withAngularVelocity(Angle.bound(1, Degree) / Second)
                .withStaticFrictionCoefficient(0.65)
                .withKineticFrictionCoefficient(0.5)
                .build();
    val pyramidFoundation = RigidBodyBuilder(AxisAlignedRectangle(Point(20, 4.5), 3, 0.5))
                            .withMass(3(Kilogram))
                            .withStaticFrictionCoefficient(0.65)
                            .withKineticFrictionCoefficient(0.5)
                            .build();
    val pyramidSecondBlock = RigidBodyBuilder(AxisAlignedRectangle(Point(20.5, 5.1), 2, 0.5))
                             .withMass(2(Kilogram))
                             .withStaticFrictionCoefficient(0.65)
                             .withKineticFrictionCoefficient(0.5)
                             .build();
    val pyramidTopBlock = RigidBodyBuilder(AxisAlignedRectangle(Point(21, 5.7), 1, 0.5))
                          .withMass(1(Kilogram))
                          .withStaticFrictionCoefficient(0.65)
                          .withKineticFrictionCoefficient(0.5)
                          .build();
    val trackedFloor = MovableTrackerProxy.track(floor);
    val trackedPyramidFoundation = MovableTrackerProxy.track(pyramidFoundation);
    val trackedPyramidSecondBlock = MovableTrackerProxy.track(pyramidSecondBlock);
    val trackedPyramidTopBlock = MovableTrackerProxy.track(pyramidTopBlock);
    
    val constantAccelerationNetGravitationalForcesResolver = ConstantAccelerationNetGravitationalForcesResolver(
        Vector(9.8, Angle.bound(270, Degree)) / Second / Second
    );
    val ignoredMatterConstantAccelerationNetGravitationalForcesResolver = IgnoredMatterNetGravitationalForcesResolver(
        resolver = constantAccelerationNetGravitationalForcesResolver,
        List(trackedFloor)
    );
    val rigidBodyDynamics = new RigidBodyDynamics[ConvexShape](ignoredMatterConstantAccelerationNetGravitationalForcesResolver);
    val dynamics = DiscreteDetectionCollidingRigidBodyDynamics[ConvexShape](
        broadPhaseCollisionDetectionStrategy = BruteForceDiscreteBroadPhaseCollisionDetectionStrategy(),
        contactDetectionStrategy = BinarySearchDiscreteConvexesContactDetectionStrategy(),
        collisionForcesResolver = RestitutionalActionReactionalCollisionForcesResolver(),
        rigidBodyDynamics = rigidBodyDynamics
    );
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    
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
    
    debugger.trackRigidBody(trackedFloor);
    debugger.trackRigidBody(trackedPyramidFoundation);
    debugger.trackRigidBody(trackedPyramidSecondBlock);
    debugger.trackRigidBody(trackedPyramidTopBlock);
    dynamics.rigidBodyDynamics.add(trackedFloor);
    dynamics.rigidBodyDynamics.add(trackedPyramidFoundation);
    dynamics.rigidBodyDynamics.add(trackedPyramidSecondBlock);
    dynamics.rigidBodyDynamics.add(trackedPyramidTopBlock);
    simulationThread.start();
  }
}