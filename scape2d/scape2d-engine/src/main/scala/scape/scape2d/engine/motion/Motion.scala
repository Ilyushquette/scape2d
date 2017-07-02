package scape.scape2d.engine.motion

import scape.scape2d.engine.core.Movable

case class Motion[T <: Movable[T]](old:T, snapshot:T, concurrent:T);