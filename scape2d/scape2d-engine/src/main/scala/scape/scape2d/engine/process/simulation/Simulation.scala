package scape.scape2d.engine.process.simulation

import org.apache.log4j.Logger
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.Instant
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.process.Process
import scape.scape2d.engine.process.ProcessOperationsDeferringProxy

object Simulation {
  def apply[T <: Process](process:T, timescale:Timescale) = {
    val processProxy = new ProcessOperationsDeferringProxy(process);
    new Simulation(processProxy.enhanced, timescale);
  }
}

class Simulation[T <: Process] private[Simulation](
  val process:T,
  var timescale:Timescale
) extends Runnable {
  private val log = Logger.getLogger(getClass);
  
  def run = {
    log.info("Simulation has been started");
    while(true) {
      val cycleStart = Instant.now();
      val timescale = this.timescale;
      val integrationDuration = timescale.integrationFrequency.occurenceDuration;
      val timestep = timescale.timestep;
      
      process.integrate(timestep);
      
      val cycleDuration = Instant.now() - cycleStart;
      val remainingTime = integrationDuration - cycleDuration;
      log.info("Cycle finished! Took %s/%s".format(cycleDuration, integrationDuration to Millisecond));
      if(remainingTime > Duration.zero) Thread.sleep(remainingTime.milliseconds.toLong);
    }
  }
}