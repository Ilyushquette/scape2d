package scape.scape2d.engine.core.matter

import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Spherical

/**
 * Units:
 * <ul>
 * 	<li>position in meters</li>
 * 	<li>radius in cantimeters</li>
 * 	<li>mass in kilograms</li>
 * 	<li>velocity - magnitude in meters, angle in degrees (meters per second at the direction)</li>
 * </ul>
 */
final class Particle private[matter] (
  private var _position:Point2D,
  var radius:Double,
  var mass:Double,
  var velocity:Vector2D,
  var forces:ArrayBuffer[Vector2D] = new ArrayBuffer)
extends Movable with Spherical {
  private val log = Logger.getLogger(getClass);
  
  def position = _position;
  
  private[core] def setPosition(nextPosition:Point2D) = _position = nextPosition;
  
  /**
   * Since each force here is a representation of impulse J = N x timestep,
   * acceleration in meters per second per timestep too.
   * Final velocity of the particle in meters per second.
   */
  def integrateForces() = {
    if(!forces.isEmpty) {
      val netforce = forces.reduce(_ + _);
      val acceleration = new Vector2D(netforce.magnitude / mass, netforce.angle);
      log.debug("Netforce %s sum of %s caused acceleration %s".format(netforce, forces, acceleration));
      velocity = velocity + acceleration;
      forces.clear();
    }
  }
}