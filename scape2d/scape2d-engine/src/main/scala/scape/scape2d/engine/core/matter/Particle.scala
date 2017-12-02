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
  private var _force:Vector2D,
  private var _bonds:Set[Bond] = Set.empty)
extends Movable with Formed[Circle] {
  private[matter] def this() = this(Circle(Point.origin, 1), 1, new Vector2D, new Vector2D);
  
  def position = _shape.center;
  
  private[core] def setPosition(nextPosition:Point) = _shape = _shape.copy(center = nextPosition);
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Vector2D) = _velocity = newVelocity;
  
  def shape = _shape;
  
  /**
   * magnitude in Newtons, angle in degrees (Newtons per last integrated timestep at the direction)
   */
  def force = _force;
  
  private[core] def exertForce(force:Vector2D) = {
    _force = _force + force;
    val bodyOpt = rotatable;
    if(bodyOpt.isDefined) {
      val body = bodyOpt.get;
      val levelArm = body.center - shape.center;
      body.exertTorque(levelArm x force);
    }
  }
  
  private[core] def resetForce() = _force = new Vector2D();
  
  def bonds = _bonds;
  
  private[core] def setBonds(bonds:Set[Bond]) = _bonds = bonds;
  
  def rotatable = bonds.firstOption.flatMap(_.body);
  
  def momentOfInertia = {
    val bodyOpt = rotatable;
    if(bodyOpt.isDefined) {
      val body = bodyOpt.get;
      val distanceToCenterOfMass = body.center distanceTo shape.center;
      distanceToCenterOfMass * distanceToCenterOfMass * mass;
    }else 0;
  }
  
  def snapshot = {
    val snapshot = snapshotExcludingBonds;
    // only snapshot of the current particle's bonds structure is taken performance wise
    snapshot._bonds = bonds.map(bond => bond.snapshot(
      particles = Combination2(snapshotExcludingBonds, bond.particles._2.snapshotExcludingBonds)
    ));
    snapshot;
  }
  
  private def snapshotExcludingBonds = new Particle(shape, mass, velocity, force);
}