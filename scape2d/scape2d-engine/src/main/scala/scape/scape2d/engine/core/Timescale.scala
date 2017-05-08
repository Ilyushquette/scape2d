package scape.scape2d.engine.core

class Timescale(val frequency:Long, val timestep:Long);

class TimescaleBuilder(val frequency:Double) {
  def <->(timestep:Double) = new Timescale(frequency.toLong, timestep.toLong);
}