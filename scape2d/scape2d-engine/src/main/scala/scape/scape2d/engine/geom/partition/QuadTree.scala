package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed

class QuadTree[E <: Formed[_ <: Shape]](
  val bounds:AxisAlignedRectangle,
  val capacity:Int
) extends Node[E] {
  private var _entities:List[E] = List.empty;
  private var _nodes:List[QuadTree[E]] = List.empty;
  
  def entities = _entities;
  
  def nodes = _nodes;
  
  def insert(entity:E):Boolean = {
    if(bounds.contains(entity.shape)) {
      val inserted = nodes.exists(_ insert entity);
      if(!inserted) insertHere(entity);
      true;
    }else false;
  }
  
  private def insertHere(entity:E) = {
    _entities = _entities :+ entity;
    if(entities.size > capacity && nodes.isEmpty) {
      val dimensions = bounds.slice(4);
      _nodes = dimensions.map(new QuadTree[E](_, capacity)).toList;
      _entities = entities.filterNot(e => nodes.exists(_.insert(e)));
    }
  }
}