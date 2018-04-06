package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.core.Identifiable
import java.util.concurrent.atomic.AtomicInteger

object FormedMock {
  private val idGenerator = new AtomicInteger(1);
  
  def nextId = idGenerator.incrementAndGet();
}

case class FormedMock[T <: Shape](
  shape:T,
  id:Int = FormedMock.nextId
) extends Formed[T] with Identifiable;