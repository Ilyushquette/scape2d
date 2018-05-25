package scape.scape2d.engine.core

import java.lang.Math.abs
import scape.scape2d.engine.time.Duration

class Temporal(timeDependent:TimeDependent) {
  private var _timeleft = timeDependent.time;
  
  def timeleft = _timeleft;
  
  private[engine] def integrate(timestep:Duration) = {
    if(timeleft > Duration.zero) {
      val adjustedTimestep = adjustTimestep(timestep);
      timeDependent.integrate(adjustedTimestep);
      _timeleft -= adjustedTimestep;
    }else throw new IllegalStateException("Temporal is outdated!");
  }
  
  private def adjustTimestep(timestep:Duration) = {
    if(timestep > timeleft) timestep - timeleft;
    else timestep;
  }
}