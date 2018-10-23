package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.time.Duration

case class ContactContainer(shape1:FiniteShape, shape2:FiniteShape, contacts:List[Contact], time:Duration);