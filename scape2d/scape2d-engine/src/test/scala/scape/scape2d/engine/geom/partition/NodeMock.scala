package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed

class NodeMock[E <: Formed[_ <: Shape]](
  val bounds:AxisAlignedRectangle = null,
  var _entities:List[E] = List.empty,
  var _nodes:List[Node[E]] = List.empty,
  var _parent:Option[Node[E]] = None
) extends Node[E] {
  def entities = _entities;
  
  def nodes = _nodes;
  
  def parent = _parent;
  
  def setParent(parent:Node[E]) = _parent = Some(parent);
  
  def insert(entity:E) = {
    _entities = _entities :+ entity;
    true;
  }
}