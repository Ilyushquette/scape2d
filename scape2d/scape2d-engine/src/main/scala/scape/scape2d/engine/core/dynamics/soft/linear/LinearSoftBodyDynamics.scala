package scape.scape2d.engine.core.dynamics.soft.linear

import scape.scape2d.engine.core.Temporal
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.core.accelerateLinear
import scape.scape2d.engine.core.dampOscillations
import scape.scape2d.engine.core.deform
import scape.scape2d.engine.core.dynamics.Dynamics
import scape.scape2d.engine.core.dynamics.soft.ParticleGravityResolver
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.moveLinear
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.IoCDeferred

class LinearSoftBodyDynamics(
  val gravityResolver:ParticleGravityResolver = ParticleGravityResolver()
) extends Dynamics {
  private var _particles = Set[Particle]();
  private var _temporals = Set[Temporal]();
  
  // this package private default constructor exists only for cglib proxy support
  private[linear] def this() = this(ParticleGravityResolver());
  
  def movables = _particles;
  
  @IoCDeferred
  def add(particle:Particle):Unit = _particles += particle;
  
  @IoCDeferred
  def add(bond:Bond):Unit = {
    add(bond.particles._1);
    add(bond.particles._2);
  };
  
  @IoCDeferred
  def add(timeDependent:TimeDependent):Unit = _temporals += new Temporal(timeDependent);
  
  def integrate(timestep:Duration) = {
    _particles.foreach(moveLinear(_, timestep));
    val bonds = _particles.flatMap(_.bonds);
    bonds.foreach(deform);
    bonds.foreach(dampOscillations);
    gravityResolver.resolve(_particles, timestep);
    _temporals.foreach(_.integrate(timestep));
    _temporals = _temporals.filter(_.timeleft > Duration.zero);
    _particles.foreach(accelerateLinear);
  }
}