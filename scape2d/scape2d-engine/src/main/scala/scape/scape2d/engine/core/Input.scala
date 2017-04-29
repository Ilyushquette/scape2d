package scape.scape2d.engine.core

import scape.scape2d.engine.matter.Particle
import scape.scape2d.engine.geom.Vector2D

abstract class Input;
case class ExertForce(particle:Particle, force:Vector2D) extends Input;