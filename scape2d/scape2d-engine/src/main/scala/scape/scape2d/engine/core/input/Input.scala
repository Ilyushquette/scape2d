package scape.scape2d.engine.core.input
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.TimeDependent

sealed trait Input;
case class ScaleTime(framerateMultiplier:Double, timestepMultiplier:Double) extends Input;
case class AddTimeSubject(timeSubject:TimeDependent) extends Input;
case class AddParticle(particle:Particle) extends Input;