package scape.scape2d.engine

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom._
import org.apache.log4j.Logger
import scape.scape2d.engine.matter.MotionEvent

package object motion {
  private val log = Logger.getLogger(getClass);
  
  private[engine] def moveParticle(fps:Int, particle:Particle) = {
    log.debug("Particle at %s with velocity %s".format(particle.position, particle.velocity));
    if(particle.velocity.magnitude > 0) {
      val oldPosition = particle.position.clone;
      val scaleMpsToMpms = scaleMagnitudePerSecond(fps, _:Vector2D);
      val move = scaleMpsToMpms andThen (_.components) andThen particle.position.displace _;
      move(particle.velocity);
      log.debug("Moved particle to %s. Prepared update pack".format(particle.position));
      List(new MotionEvent(oldPosition, particle));
    }else Nil;
  }
  
  /**
   * Acceleration in meters per second per interval_between_instants.
   * Final velocity of the particle in meters per second.
   */
  private[engine] def applyForces(particle:Particle, forces:Iterable[Vector2D]) = forces match {
    case Nil => log.debug("Forces are empty");
    case forces => {
      val netforce = forces.reduce(_ + _);
      val acceleration = new Vector2D(netforce.magnitude / particle.mass, netforce.angle);
      log.debug("Netforce %s sum of %s caused acceleration %s".format(netforce, forces, acceleration));
      particle.velocity = particle.velocity + acceleration;
    }
  }
  
  private def scaleMagnitudePerSecond(fps:Int, vector:Vector2D) = {
    new Vector2D(vector.magnitude / fps, vector.angle);
  }
  
  def getPositionAfter(movable:Movable, timestep:Long) = {
    if(timestep <= 0) throw new IllegalArgumentException("Time is irreversible. Timestep=" + timestep);
    val currentPosition = movable.position.clone;
    if(movable.velocity.magnitude > 0) {
      val mpms = scaleVelocity(movable.velocity, timestep);
      currentPosition.displace(mpms.components);
      currentPosition;
    }else currentPosition;
  }
  
  private[engine] def integrateMotion(movable:Movable, timestep:Long) = {
    log.debug("Movable at %s with velocity %s".format(movable.position, movable.velocity));
    val nextPosition = getPositionAfter(movable, timestep);
    if(movable.position != nextPosition) {
      val oldPosition = movable.position.clone;
      movable.position.locate(nextPosition);
      log.debug("Moved to " + nextPosition);
      movable.motionListeners.foreach(_(oldPosition, movable));
    }
  }
  
  def scaleVelocity(velocity:Vector2D, timestep:Long) = {
    val timescale = 1000 / timestep;
    new Vector2D(velocity.magnitude / timescale, velocity.angle);
  }
}