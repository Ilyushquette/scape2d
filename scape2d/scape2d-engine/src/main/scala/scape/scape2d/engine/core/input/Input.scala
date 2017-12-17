package scape.scape2d.engine.core.input
import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.core.matter.Bond
import scape.scape2d.engine.core.matter.Body

sealed trait Input;
case class AddTimeSubject(timeSubject:TimeDependent) extends Input;
case class AddParticle(particle:Particle) extends Input;
case class AddBond(bond:Bond) extends Input;
case class AddBody(body:Body) extends Input;