package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Shape

class MovablePhantom[T <: Movable with Formed[_ <: Shape] with Identifiable](
  val origin:T
) extends Movable with Formed[Shape] with Identifiable {
  val id = origin.id;
  private var _phantomPosition = origin.position;
  
  def position = _phantomPosition;
  
  private[core] def setPosition(newPosition:Point) = _phantomPosition = newPosition;
  
  def shape = origin.shape.displacedBy(position - origin.position);
  
  def velocity = origin.velocity;
  
  private[core] def setVelocity(newVelocity:Vector) = origin.setVelocity(newVelocity);
  
  def rotatable = origin.rotatable;
  
  def snapshot = {
    val snapshot = new MovablePhantom(origin);
    snapshot.setPosition(position);
    snapshot;
  }
  
  private[core] def commitPosition() = origin.setPosition(position);
}