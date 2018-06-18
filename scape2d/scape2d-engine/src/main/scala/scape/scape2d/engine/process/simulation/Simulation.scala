package scape.scape2d.engine.process.simulation

import scala.actors.Actor
import scala.actors.TIMEOUT
import org.apache.log4j.Logger
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.Instant
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond

abstract class Simulation(
  var timescale:Timescale
) extends Actor {
  private val log = Logger.getLogger(getClass);
  
  override def act = {
    log.info("Simulation has been started");
    loop {
      val cycleStart = Instant.now();
      val timescale = this.timescale;
      val integrationDuration = timescale.integrationFrequency.occurenceDuration;
      val timestep = timescale.timestep;
      
      integrate(timestep);
      dispatchInputs();
      
      val cycleDuration = Instant.now() - cycleStart;
      val remainingTime = integrationDuration - cycleDuration;
      log.info("Cycle finished! Took %s/%s".format(cycleDuration, integrationDuration to Millisecond));
      if(remainingTime > Duration.zero) Thread.sleep(remainingTime.milliseconds.toLong);
    }
  }
  
  def integrate(timestep:Duration);
  
  private def dispatchInputs():Unit = receiveWithin(0) {
    case act:(() => Unit) =>
      act();
      dispatchInputs();
    case TIMEOUT => return;
    case unknown =>
      log.warn("Unknown input " + unknown);
      dispatchInputs();
  }
}