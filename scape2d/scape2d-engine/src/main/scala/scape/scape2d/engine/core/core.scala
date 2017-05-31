package scape.scape2d.engine

import org.apache.log4j.Logger

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.getPositionAfter

package object core {
  private val log = Logger.getLogger(getClass);
  
  private[core] def integrateMotion(movable:Movable, timestep:Double) = {
    log.debug("Movable at %s with velocity %s".format(movable.position, movable.velocity));
    val nextPosition = getPositionAfter(movable, timestep);
    if(movable.position != nextPosition) {
      movable.setPosition(nextPosition);
      log.debug("Moved to " + nextPosition);
    }
  }
}