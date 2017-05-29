package scape.scape2d.engine.core

import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.matter.Particle

abstract class Input;
case class ExertForce(particle:Particle, force:Vector2D) extends Input;
case class ScaleTime(framerateMultiplier:Double, timestepMultiplier:Double) extends Input;