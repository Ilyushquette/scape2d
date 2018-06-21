package scape.scape2d.engine.core

import scape.scape2d.engine.core.integral.MotionIntegral
import scape.scape2d.engine.core.matter.Body
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.integral.ContinuousMotionIntegral
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.process.Process
import scape.scape2d.engine.time.IoCDeferred

class Nature(
  val motionIntegral:MotionIntegral
) extends Process {
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def this() = this(ContinuousMotionIntegral());
  
  @IoCDeferred
  def add(timeDependent:TimeDependent):Unit = temporals += new Temporal(timeDependent);
  
  @IoCDeferred
  def add(particle:Particle):Unit = particles += particle;
  
  def add(bond:Bond):Unit = {
    add(bond.particles._1);
    add(bond.particles._2);
  };
  
  def add(body:Body):Unit = body.movables.foreach(add);
  
  def integrate(timestep:Duration) = {
    motionIntegral.integrate(particles, timestep);
    temporals.foreach(_.integrate(timestep));
    temporals = temporals.filter(_.timeleft > Duration.zero);
  }
}