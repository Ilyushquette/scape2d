package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.Combination2

class Particle private[matter] (
  private var _shape:Circle,
  val mass:Double,
  private var _velocity:Vector2D,
  private var _forces:Array[Vector2D],
  private var _bonds:Set[Bond] = Set.empty)
extends Movable with Formed[Circle] {
  private[matter] def this() = this(Circle(Point.origin, 1), 1, new Vector2D, Array.empty);
  
  def position = _shape.center;
  
  private[core] def setPosition(nextPosition:Point) = _shape = _shape.copy(center = nextPosition);
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Vector2D) = _velocity = newVelocity;
  
  def shape = _shape;
  
  /**
   * magnitude in Newtons, angle in degrees (Newtons per last integrated timestep at the direction)
   */
  def forces = _forces;
  
  private[core] def setForces(forces:Array[Vector2D]) = _forces = forces;
  
  def bonds = _bonds;
  
  private[core] def setBonds(bonds:Set[Bond]) = _bonds = bonds;
  
  def rotatable = bonds.firstOption.flatMap(_.body);
  
  def snapshot = {
    val snapshot = snapshotExcludingBonds;
    // only snapshot of the current particle's bonds structure is taken performance wise
    snapshot._bonds = bonds.map(bond => bond.snapshot(
      particles = Combination2(snapshotExcludingBonds, bond.particles._2.snapshotExcludingBonds)
    ));
    snapshot;
  }
  
  private def snapshotExcludingBonds = new Particle(shape, mass, velocity, forces);
}