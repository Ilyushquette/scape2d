package scape.scape2d.engine.core.dynamics.rigid

import scape.scape2d.engine.core.Temporal
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.RigidBody
import scape.scape2d.engine.core.move
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.IoCDeferred
import scape.scape2d.engine.geom.shape.FiniteShape

class RigidBodyDynamics[T >: Null <: FiniteShape] extends Dynamics {
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
    _temporals.foreach(_ integrate timestep);
    _temporals = _temporals.filter(_.timeleft > Duration.zero);
  }
}