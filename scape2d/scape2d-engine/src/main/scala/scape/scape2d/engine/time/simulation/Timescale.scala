package scape.scape2d.engine.time.simulation

import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Frequency

object Timescale {
  def apply(integrationFrequency:Frequency) = new Timescale(integrationFrequency, integrationFrequency.occurenceDuration);
}

case class Timescale(integrationFrequency:Frequency, timestep:Duration);