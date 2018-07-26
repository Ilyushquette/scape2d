package scape.scape2d.engine.core.dynamics.soft

import scape.scape2d.engine.core.Temporal
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.core.accelerate
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.matter.Body
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.move
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.IoCDeferred

class SoftBodyDynamics(
  val gravityResolver:ParticleGravityResolver = ParticleGravityResolver()
) extends Dynamics {
  private var _particles = Set[Particle]();
  private var _temporals = Set[Temporal]();
  
  // this package private default constructor exists only for cglib proxy support
  private[soft] def this() = this(ParticleGravityResolver());
  
  def movables = _particles;
  
  @IoCDeferred
  def add(particle:Particle):Unit = _particles += particle;
  
  @IoCDeferred
  def add(bond:Bond):Unit = {
    add(bond.particles._1);
    add(bond.particles._2);
  };
  
  @IoCDeferred
  def add(body:Body):Unit = body.movables.foreach(add);
  
  @IoCDeferred
  def add(timeDependent:TimeDependent):Unit = _temporals += new Temporal(timeDependent);
  
  def integrate(timestep:Duration) = {
    _particles.foreach(move(_, timestep));
    val bonds = _particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
    gravityResolver.resolve(_particles, timestep);
    _temporals.foreach(_.integrate(timestep));
    _temporals = _temporals.filter(_.timeleft > Duration.zero);
    _particles.foreach(accelerate);
  }
}