package scape.scape2d.engine.core.dynamics.rigid

import scape.scape2d.engine.core.Temporal
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.core.move
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.gravity.NetGravitationalForcesResolver
import scape.scape2d.engine.gravity.UniversalNetGravitationalForcesResolver

class RigidBodyDynamics[T >: Null <: FiniteShape](
  val netGravitationalForcesResolver:NetGravitationalForcesResolver = UniversalNetGravitationalForcesResolver()
) extends Dynamics {
  private var _rigidBodies = Set[RigidBody[_ <: T]]();
  private var _temporals = Set[Temporal]();
  
  def movables = _rigidBodies;
  
  def temporals = _temporals;
  
  @IoCDeferred
  def add(rigidBody:RigidBody[_ <: T]):Unit = _rigidBodies += rigidBody;
  
  @IoCDeferred
  def add(timeDependent:TimeDependent):Unit = _temporals += new Temporal(timeDependent);
  
  def integrate(timestep:Duration) = {
    _rigidBodies.foreach(move(_, timestep));
    exertGravitationalForces(timestep);
    _temporals.foreach(_ integrate timestep);
    _temporals = _temporals.filter(_.timeleft > Duration.zero);
  }
  
  def exertGravitationalForces(timestep:Duration) = {
    val netGravitationalForces = netGravitationalForcesResolver.resolve(_rigidBodies, timestep);
    netGravitationalForces foreach {
      case (rigidBody, netGravitationalForce) => rigidBody.exertForce(netGravitationalForce, rigidBody.center);
    }
  }
}