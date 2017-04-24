package scape.scape2d.engine

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom._
import org.apache.log4j.Logger
import scape.scape2d.engine.matter.MotionEvent

package object motion {
  private val log = Logger.getLogger(getClass);
  
  private[engine] def createMotionTransformer(fps:Int) = {
    particle:Particle => {
      log.debug("Particle at %s with velocity %s".format(particle.position, particle.velocity));
      if(particle.velocity.magnitude > 0) {
        val oldPosition = particle.position.clone;
        val scaleMpsToMpms = scaleMagnitudePerSecond(fps, _:Vector2D);
        val move = scaleMpsToMpms andThen calculateComponents andThen particle.position.displace _;
        move(particle.velocity);
        log.debug("Moved particle to %s. Prepared update pack".format(particle.position));
        List(new MotionEvent(oldPosition, particle));
      }else Nil;
    };
  }
  
  private def scaleMagnitudePerSecond(fps:Int, vector:Vector2D) = {
    new Vector2D(vector.magnitude / fps, vector.angle);
  }
}