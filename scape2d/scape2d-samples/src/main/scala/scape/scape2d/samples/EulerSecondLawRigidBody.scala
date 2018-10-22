package scape.scape2d.samples

import java.awt.Color
import java.awt.Toolkit

import javax.swing.JFrame
import scape.scape2d.debugger.RigidBodyDebugger
import scape.scape2d.debugger.view.ShapeDrawingRigidBodyTrackingView
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.core.MovableTrackerProxy
import scape.scape2d.engine.core.dynamics.rigid.RigidBodyDynamics
import scape.scape2d.engine.core.matter.RigidBodyBuilder
import scape.scape2d.engine.core.matter.TorqueImpulseOntoRigidBody
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.engine.geom.shape.FiniteShape

object EulerSecondLawRigidBody {
  def main(args:Array[String]) = {
    val dynamics = new RigidBodyDynamics[FiniteShape]();
    val simulation = new Simulation(dynamics);
    val simulationThread = new Thread(simulation);
    
    val circleSweep = CircleSweep(Circle(Point(3, 14), 0.5), Vector(4, 270(Degree)));
    val rigidBody = RigidBodyBuilder(circleSweep)
                    .withMass(Mass(10, Kilogram))
                    .build();
    
    val trackedRigidBody = MovableTrackerProxy.track(rigidBody);
    
    val torqueImpulseClockwise = TorqueImpulseOntoRigidBody[CircleSweep](
        rigidBody = trackedRigidBody,
        torque = -25,
        pointOfApplication = _.shape.circle.center,
        Duration(15, Second)
    );
    val torqueImpulseCounterClockwise = TorqueImpulseOntoRigidBody[CircleSweep](
        rigidBody = trackedRigidBody,
        torque = 25,
        pointOfApplication = _.shape.circle.center,
        Duration(15, Second)
    );
    
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
    
    debugger.trackRigidBody(trackedRigidBody);
    dynamics.add(trackedRigidBody);
    dynamics.add(torqueImpulseClockwise);
    simulationThread.start();
    
    Thread.sleep(15000);
    dynamics.add(torqueImpulseCounterClockwise);
  }
}