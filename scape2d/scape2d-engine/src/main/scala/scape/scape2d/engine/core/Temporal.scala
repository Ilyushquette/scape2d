package scape.scape2d.engine.core

class Temporal(timeDependent:TimeDependent) {
  private var _timeleft = timeDependent.time.milliseconds;
  
  def timeleft = _timeleft;
  
  private[engine] def integrate(timestep:Double) = {
    if(timeleft > 0) {
      val adjustedTimestep = adjustTimestep(timestep);
      timeDependent.integrate(adjustedTimestep);
      _timeleft -= adjustedTimestep;
    }else throw new IllegalStateException("Temporal is outdated!");
  }
  
  private def adjustTimestep(timestep:Double) = {
    if(timestep > timeleft) Math.abs(timestep - timeleft);
    else timestep;
  }
}