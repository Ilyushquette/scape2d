package scape.scape2d.engine.core.matter

import org.apache.log4j.Logger

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Spherical
import scape.scape2d.engine.geom.Vector2D

/**
 * mass in kilograms
 */
class Particle private[matter] (
  private var _position:Point2D,
  val radius:Double,
  val mass:Double,
  private var _velocity:Vector2D,
  private var _forces:Array[Vector2D])
extends Movable with Spherical {
  private val log = Logger.getLogger(getClass);
  
  private[matter] def this() = this(Point2D.origin, 1, 1, new Vector2D, Array.empty);
  
  def position = _position;
  
  private[core] def setPosition(nextPosition:Point2D) = _position = nextPosition;
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Vector2D) = _velocity = newVelocity;
  
  /**
   * magnitude in Newtons, angle in degrees (Newtons per last integrated timestep at the direction)
   */
  def forces = _forces;
  
  private[core] def setForces(forces:Array[Vector2D]) = _forces = forces;
}