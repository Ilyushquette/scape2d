package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Spherical

case class Collision[T <: Movable with Spherical](pair:(T, T), time:Double);