package scape.scape2d.engine.core

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import scape.scape2d.engine.motion.collision._
import scape.scape2d.engine.motion.collision.detection._
import scape.scape2d.engine.motion._
import scape.scape2d.engine.core.matter.Particle

object Nature {
  type CollisionDetector = (Iterable[Particle], Double) => Iterator[Collision[Particle]];
}

class Nature(val detectCollisions:Nature.CollisionDetector, fps:Double) extends Actor {
  private val log = Logger.getLogger(getClass);
  private val timeSubjects = new ArrayBuffer[TimeDependent];
  private val particles = new ArrayBuffer[Particle];
  private var timescale = scaleTime(1, 1);
  
  def this(fps:Double) = this(bruteForce(detectWithDiscriminant _), fps);
  
  def add(timeSubject:TimeDependent) = timeSubjects += timeSubject;
  
  def add(particle:Particle) = particles += particle;
  
  private def scaleTime(fm:Double, tm:Double) = 1000 / (fps * fm) <-> 1000 / fps * tm;
  
  implicit def toTimescaleBuilder(frequency:Double):TimescaleBuilder = new TimescaleBuilder(frequency);
  
  override def act = {
    log.info("Nature has been started");
    loop {
      val cycleStart = System.currentTimeMillis;
      integrate(timescale.timestep);
      dispatchInputs();
      val cycleMillis = System.currentTimeMillis - cycleStart;
      val cooldown = timescale.frequency - cycleMillis;
      log.debug("Cycle finished! Took %d/%d ms".format(cycleMillis, timescale.frequency));
      Thread.sleep(if (cooldown > 0) cooldown else 0);
    }
  }
  
  private def integrate(timestep:Double):Unit = {
    log.debug("Time integration phase starts...");
    particles.foreach(_.integrateForces());
    val collisions = detectCollisions(particles, timestep);
    if(!collisions.isEmpty) {
      val earliestCollision = collisions.minBy(_.time);
      log.debug("Earliest COLLISION DETECTED! Time left: " + earliestCollision.time);
      val integratedTime = handleCollisionAndIntegrate(earliestCollision);
      integrate(timestep - integratedTime);
    }else integrateMotionAndSubjects(timestep);
    log.debug("Time integration phase ended.");
  }
  
  private def handleCollisionAndIntegrate(collision:Collision[Particle]) = {
    val safeTime = findSafeTime(collision, 0.005);
    log.debug(safeTime + " time safe to be integrated to prevent intersection");
    val forces = resolveForces(collision);
    if(safeTime > 0) {
      integrateMotionAndSubjects(safeTime);
      collision.pair._1.forces += forces._1;
      collision.pair._2.forces += forces._2;
    }
    safeTime;
  }
  
  private def integrateMotionAndSubjects(timestep:Double) = {
    particles.foreach(integrateMotion(_, timestep));
    timeSubjects.foreach(_.integrate(timestep));
  }
  
  private def dispatchInputs() = {
    log.debug("Input dispatching phase starts...");
    var endOfMailbox = false;
    while(!endOfMailbox) {
      receiveWithin(0) {
        case ExertForce(p, f) => p.forces += f;
        case ScaleTime(fm, tm) => timescale = scaleTime(fm, tm);
        case TIMEOUT => endOfMailbox = true;
        case unknown => log.warn("Unknown input " + unknown);
      }
    }
    log.debug("Input dispatching phase ended.");
  }
}