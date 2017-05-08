package scape.scape2d.engine.matter

import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D
import org.apache.log4j.Logger
import scala.collection.mutable.ArrayBuffer

/**
 * Units:
 * <ul>
 * 	<li>position in meters</li>
 * 	<li>radius in cantimeters</li>
 * 	<li>mass in kilograms</li>
 * 	<li>velocity - magnitude in meters, angle in degrees (meters per second at the direction)</li>
 * </ul>
 */
class Particle(val position:Point2D, val radius:Double, val mass:Double, var velocity:Vector2D) {
  private[engine] val motionListeners = new LinkedHashSet[MotionEvent => Unit];
  private val log = Logger.getLogger(getClass);
  private[engine] val forces = new ArrayBuffer[Vector2D];
  
  def addMotionListener(listener:MotionEvent => Unit) = motionListeners.add(listener);
  
  def removeMotionListener(listener:MotionEvent => Unit) = motionListeners.remove(listener);
  
  /**
   * Since each force here is a representation of impulse J = N x timestep,
   * acceleration in meters per second per timestep too.
   * Final velocity of the particle in meters per second.
   */
  def integrateForces(timestep:Long) = {
    if(!forces.isEmpty) {
      val netforce = forces.reduce(_ + _);
      val acceleration = new Vector2D(netforce.magnitude / mass, netforce.angle);
      log.debug("Netforce %s sum of %s caused acceleration %s".format(netforce, forces, acceleration));
      velocity = velocity + acceleration;
      forces.clear();
    }
  }
}