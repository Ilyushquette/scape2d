package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Shape

case class FormedMock[T <: Shape](shape:T) extends Formed[T];