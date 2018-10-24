package scape.scape2d.engine.core.matter

import java.util.concurrent.atomic.AtomicInteger

import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.density.Density
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.MassUnit.toMass
import scape.scape2d.engine.mass.angular.MomentOfInertia
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity

object RigidBody {
  private val idGenerator = new AtomicInteger(1);
  
  private[matter] def nextId = idGenerator.incrementAndGet();
}

/**
 * NOTE: Type parameter T which has upper bound of FiniteShape
 * ALSO has lower bound of Null to allow package private default constructor to initialize
 * rigid body with null shape in order to support cglib proxying
 */
class RigidBody[T >: Null <: FiniteShape] private[matter](
  val id:Int,
  private var _shape:T,
  val mass:Mass,
  private var _velocity:Velocity,
  private var _angularVelocity:AngularVelocity
) extends Movable[T] with Rotatable with Identifiable {
  lazy val density = Density(mass, shape.area);
  
  // this package private default constructor exists only for cglib proxy support
  private[matter] def this() = this(RigidBody.nextId, null, Kilogram, Velocity.zero, AngularVelocity.zero);
  
  def position = _shape.center;
  
  def center = position;
  
  def shape = _shape;
  
  private[core] def setShape(newShape:T) = _shape = newShape;
  
  def velocity = _velocity;
  
  private[core] def setVelocity(newVelocity:Velocity) = _velocity = newVelocity;
  
  def rotatable = Some(this);
  
  def angularVelocity = _angularVelocity;
  
  private[core] def setAngularVelocity(newAngularVelocity:AngularVelocity) = _angularVelocity = newAngularVelocity;
  
  lazy val momentOfInertia = MomentOfInertia(density, shape);
  
  private[core] def exertForce(force:Vector, pointOnBody:Point) = {
    val leverArm = pointOnBody - shape.center;
    if(pointOnBody == center) {
      val instantAcceleration = mass forForce force;
      _velocity += instantAcceleration.velocity;
    }else{
      val forceAlongLeverArm = force projection leverArm;
      val instantAcceleration = mass forForce forceAlongLeverArm;
      _velocity += instantAcceleration.velocity;
      exertTorque(leverArm x force);
    }
  }
  
  private[core] def exertTorque(torque:Double) = {
    val instantAngularAcceleration = momentOfInertia forTorque torque;
    _angularVelocity += instantAngularAcceleration.angularVelocity;
  }
  
  def movables = Set(this);
  
  def snapshot = snapshot();
  
  def snapshot(shape:T = _shape,
               mass:Mass = this.mass,
               velocity:Velocity = _velocity,
               angularVelocity:AngularVelocity = _angularVelocity) = {
    new RigidBody(id, shape, mass, velocity, angularVelocity);
  }
}