package scape.scape2d.engine.time.simulation

import scala.actors.Actor
import scala.actors.TIMEOUT
import org.apache.log4j.Logger
import scape.scape2d.engine.core.Timescale
import scape.scape2d.engine.time.TimeUnit.toDuration
import scape.scape2d.engine.time.Frequency
import scape.scape2d.engine.time.Second

abstract class Simulation(
  var timescale:Timescale
) extends Actor {
  private val log = Logger.getLogger(getClass);
  
  override def act = {
    log.info("Simulation has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      val timescale = this.timescale;
      val integrationMillis = timescale.integrationFrequency.occurenceDuration.milliseconds;
      val timestepMillis = timescale.timestep.milliseconds;
      
      integrate(timestepMillis);
      dispatchInputs();
      
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = (integrationMillis - cycleMillis).toLong;
      log.info("Cycle finished! Took %d/%f ms".format(cycleMillis, integrationMillis));
      if(cooldown > 0) Thread.sleep(cooldown);
    }
  }
  
  def integrate(timestep:Double);
  
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