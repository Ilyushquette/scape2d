package scape.scape2d.engine.core.mock

import java.util.concurrent.atomic.AtomicInteger

import scape.scape2d.engine.core.Identifiable
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.core.Rotatable
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.linear.Velocity

object MovableMock {
  private val idGenerator = new AtomicInteger(1);
  
  private def nextId = idGenerator.incrementAndGet();
}

private[engine] class MovableMock(
  var shape:Circle,
  var velocity:Velocity,
  private var _rotatable:Option[Rotatable])
extends Movable[Circle] with Identifiable {
  val id = MovableMock.nextId;
  
  // this default constructor is only exists for cglib proxy support
  def this() = this(Circle(Point.origin, 0), Velocity.zero, None);
  
  def this(position:Point, velocity:Velocity, rotatable:Option[Rotatable]) = {
    this(Circle(position, 0), velocity, rotatable);
  }
  
  def position = shape.center;
  
  def setShape(newShape:Circle) = shape = newShape;
  
  def setVelocity(newVelocity:Velocity) = velocity = newVelocity;
  
  def rotatable = _rotatable;
  
  def setRotatable(newRotatable:Rotatable) = _rotatable = Some(newRotatable);
  
  def snapshot = new MovableMock(shape, velocity, rotatable);
}