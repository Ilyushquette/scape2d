package scape.scape2d.engine.core.matter

import java.util.concurrent.atomic.AtomicInteger

import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.gravity.G
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.angular.MomentOfInertia
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.util.Combination2

object Particle {
  private val idGenerator = new AtomicInteger(1);
  
  private[matter] def nextId = idGenerator.incrementAndGet();
}

class Particle private[matter] (
  val id:Int,
  private var _shape:Circle,
  val mass:Mass,
  private var _velocity:Velocity,
  private var _force:Vector,
  private var _bonds:Set[Bond] = Set.empty)
extends Movable[Circle] with Identifiable {
  private[matter] def this() = this(Particle.nextId, Circle(Point.origin, 1), Mass(1, Kilogram), Velocity.zero, Vector.zero);
  
  def position = _shape.center;
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Velocity) = _velocity = newVelocity;
  
  def shape = _shape;
  
  private[core] def setShape(newShape:Circle) = _shape = newShape;
  
  /**
   * magnitude in Newtons, angle in degrees (Newtons per last integrated timestep at the direction)
   */
  def force = _force;
  
  private[core] def exertForce(force:Vector, cascade:Boolean) = {
    _force = _force + force;
    val bodyOpt = rotatable;
    if(cascade && bodyOpt.isDefined) {
      val body = bodyOpt.get;
      val leverArm = shape.center - body.center;
      body.exertTorque(leverArm x force);
    }
  }
  
  private[core] def resetForce() = _force = Vector.zero;
  
  def bonds = _bonds;
  
  private[core] def setBonds(bonds:Set[Bond]) = _bonds = bonds;
  
  def rotatable = bonds.headOption.flatMap(_.body);
  
  def momentOfInertia = rotatable.map(r => MomentOfInertia(mass, r.center distanceTo position));
  
  def gravitationalForceOnto(particle:Particle, timestep:Duration) = {
    val r = position distanceTo particle.position;
    if(r != 0) {
      val m1 = mass.kilograms;
      val m2 = particle.mass.kilograms;
      val gravitationalForcePerSecond = Vector(G * ((m1 * m2) / (r * r)), particle.position angleTo position);
      gravitationalForcePerSecond * (timestep / Second);
    }else Vector.zero;
  }
  
  override def hashCode = id;
  
  override def equals(any:Any) = any match {
    case particle:Particle => id == particle.id;
    case _ => false;
  }
  
  def snapshot = snapshot();
  
  def snapshot(shape:Circle = this.shape,
               mass:Mass = this.mass,
               velocity:Velocity = this.velocity,
               force:Vector = this.force) = {
    val snapshot = snapshotExcludingBonds(shape, mass, velocity, force);
    // only snapshot of the current particle's bonds structure is taken performance wise
    snapshot._bonds = bonds.map(bond => bond.snapshot(
      particles = Combination2(snapshot, bond.particles._2.snapshotExcludingBonds)
    ));
    snapshot;
  }
  
  def snapshotExcludingBonds:Particle = snapshotExcludingBonds();
  
  def snapshotExcludingBonds(shape:Circle = this.shape,
                             mass:Mass = this.mass,
                             velocity:Velocity = this.velocity,
                             force:Vector = this.force):Particle = {
    new Particle(id, shape, mass, velocity, force);
  }
}