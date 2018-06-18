package scape.scape2d.engine.core

import scape.scape2d.engine.core.integral.MotionIntegral
import scape.scape2d.engine.core.matter.Body
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.process.simulation.Simulation
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.process.simulation.Timescale
import scape.scape2d.engine.core.integral.ContinuousMotionIntegral
import scape.scape2d.engine.time.Duration

class Nature(
  timeScale:Timescale = Timescale(Frequency(60, Second)),
  val motionIntegral:MotionIntegral = ContinuousMotionIntegral()
) extends Simulation(timeScale) {
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def add(timeDependent:TimeDependent):Unit = this ! (() => temporals += new Temporal(timeDependent));
  
  def add(particle:Particle):Unit = this ! (() => particles += particle);
  
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