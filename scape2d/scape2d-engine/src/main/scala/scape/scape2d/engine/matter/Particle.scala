package scape.scape2d.engine.matter

import scala.collection.mutable.LinkedHashSet

import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D

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
  
  def addMotionListener(listener:MotionEvent => Unit) = motionListeners.add(listener);
  
  def removeMotionListener(listener:MotionEvent => Unit) = motionListeners.remove(listener);
}