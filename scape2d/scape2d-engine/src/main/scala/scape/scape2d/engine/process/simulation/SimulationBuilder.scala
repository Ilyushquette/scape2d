package scape.scape2d.engine.process.simulation

import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.process.Process
import org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor

case class SimulationBuilder(
  timescale:Timescale = Timescale(Frequency(60, Second))
) {
  def withTimescale(ts:Timescale) = copy(timescale = ts);
  
  def build[T <: Process](processClass:Class[T], arguments:Object*) = {
    val process = invokeConstructor(processClass, arguments:_*);
    Simulation(process, timescale);
  }
}