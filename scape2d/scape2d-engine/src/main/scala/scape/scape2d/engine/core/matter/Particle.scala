package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.core.Identifiable
import java.util.concurrent.atomic.AtomicInteger
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.time.Second

object Particle {
  private val idGenerator = new AtomicInteger(1);
  
  private[matter] def nextId = idGenerator.incrementAndGet();
}

class Particle private[matter] (
  val id:Int,
  private var _shape:Circle,
  val mass:Double,
  private var _velocity:Velocity,
  private var _force:Vector,
  private var _bonds:Set[Bond] = Set.empty)
extends Movable with Formed[Circle] with Identifiable {
  private[matter] def this() = this(Particle.nextId, Circle(Point.origin, 1), 1, Velocity.zero, Vector.zero);
  
  def position = _shape.center;
  
  private[core] def setPosition(nextPosition:Point) = _shape = _shape.copy(center = nextPosition);
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Velocity) = _velocity = newVelocity;
  
  def shape = _shape;
  
  /**
   * magnitude in Newtons, angle in degrees (Newtons per last integrated timestep at the direction)
   */
  def force = _force;
  
  private[core] def exertForce(force:Vector, cascade:Boolean) = {
    _force = _force + force;
    val bodyOpt = rotatable;
    if(cascade && bodyOpt.isDefined) {
      val body = bodyOpt.get;
      val levelArm = shape.center - body.center;
      body.exertTorque(levelArm x force);
    }
  }
  
  private[core] def resetForce() = _force = Vector.zero;
  
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
  
  def snapshot = snapshot();
  
  def snapshot(shape:Circle = this.shape,
               mass:Double = this.mass,
               velocity:Velocity = this.velocity,
               force:Vector = this.force) = {
    val snapshot = snapshotExcludingBonds(shape, mass, velocity, force);
    // only snapshot of the current particle's bonds structure is taken performance wise
    snapshot._bonds = bonds.map(bond => bond.snapshot(
      particles = Combination2(snapshot, bond.particles._2.snapshotExcludingBonds)
    ));
    snapshot;
  }
  
  private def snapshotExcludingBonds:Particle = snapshotExcludingBonds();
  
  private def snapshotExcludingBonds(shape:Circle = this.shape,
                                     mass:Double = this.mass,
                                     velocity:Velocity = this.velocity,
                                     force:Vector = this.force):Particle = {
    new Particle(id, shape, mass, velocity, force);
  }
}