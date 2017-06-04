package scape.scape2d.engine.core

import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.matter.Particle

abstract class Input;
case class ScaleTime(framerateMultiplier:Double, timestepMultiplier:Double) extends Input;
case class AddTimeSubject(timeSubject:TimeDependent) extends Input;
case class AddParticle(particle:Particle) extends Input;