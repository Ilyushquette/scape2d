package scape.scape2d.engine.core

import scape.scape2d.engine.core.integral.LinearMotionIntegral
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.simulation.Simulation
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.simulation.Timescale
import scape.scape2d.engine.time.Duration

class NonRotatableNature(
  timeScale:Timescale = Timescale(Frequency(60, Second)),
  val linearMotionIntegral:LinearMotionIntegral = LinearMotionIntegral()
) extends Simulation(timeScale) {
  private var temporals = Set[Temporal]();
  private var particles = Set[Particle]();
  
  def add(timeDependent:TimeDependent):Unit = this ! (() => temporals += new Temporal(timeDependent));
  
  def add(particle:Particle):Unit = this ! (() => particles += particle);
  
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