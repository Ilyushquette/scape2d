package scape.scape2d.engine

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom._
import org.apache.log4j.Logger

package object motion {
  private val log = Logger.getLogger(getClass);
  
  def getPositionAfter(movable:Movable, timestep:Double) = {
    if(timestep <= 0) throw new IllegalArgumentException("Time is irreversible. Timestep=" + timestep);
    val currentPosition = movable.position.clone;
    if(movable.velocity.magnitude > 0) {
      val mpms = scaleVelocity(movable.velocity, timestep);
      currentPosition.displace(mpms.components);
      currentPosition;
    }else currentPosition;
  }
  
  private[engine] def integrateMotion(movable:Movable, timestep:Double) = {
    log.debug("Movable at %s with velocity %s".format(movable.position, movable.velocity));
    val nextPosition = getPositionAfter(movable, timestep);
    if(movable.position != nextPosition) {
      val oldPosition = movable.position.clone;
      movable.position.locate(nextPosition);
      log.debug("Moved to " + nextPosition);
      movable.motionListeners.foreach(_(oldPosition, movable));
    }
  }
  
  def scaleVelocity(velocity:Vector2D, timestep:Double) = {
    val timescale = 1000 / timestep;
    new Vector2D(velocity.magnitude / timescale, velocity.angle);
  }
}