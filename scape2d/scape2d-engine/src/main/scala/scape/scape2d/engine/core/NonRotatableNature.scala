package scape.scape2d.engine.core

import scape.scape2d.engine.core.integral.LinearMotionIntegral
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.process.Process
import scape.scape2d.engine.time.IoCDeferred

class NonRotatableNature(
  val linearMotionIntegral:LinearMotionIntegral
) extends Process {
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def this() = this(LinearMotionIntegral());
  
  @IoCDeferred
  def add(timeDependent:TimeDependent):Unit = temporals += new Temporal(timeDependent);
  
  @IoCDeferred
  def add(particle:Particle):Unit = particles += particle;
  
  def add(bond:Bond):Unit = {
    add(bond.particles._1);
    add(bond.particles._2);
  };
  
  def integrate(timestep:Duration) = {
    linearMotionIntegral.integrate(particles, timestep);
    temporals.foreach(_.integrate(timestep));
    temporals = temporals.filter(_.timeleft > Duration.zero);
  }
}