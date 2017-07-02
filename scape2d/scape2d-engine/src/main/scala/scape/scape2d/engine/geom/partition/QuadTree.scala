package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed

class QuadTree[E <: Formed[_ <: Shape]](
  val bounds:AxisAlignedRectangle,
  val capacity:Int) {
  private var _entities:Seq[E] = Seq.empty;
  private var _nodes:Seq[QuadTree[E]] = Seq.empty;
  
  def entities = _entities;
  
  def subEntities:Seq[E] = entities ++ nodes.flatMap(_.subEntities);
  
  def nodes = _nodes;
  
  def insert(entity:E):Boolean = {
    bounds.contains(entity.shape) &&
    (!nodes.isEmpty && nodes.exists(_.insert(entity)) || insertHere(entity));
  }
  
  private def insertHere(entity:E) = {
    _entities = _entities :+ entity;
    if(entities.size > capacity && nodes.isEmpty) {
      val dimensions = bounds.slice(4);
      _nodes = dimensions.map(new QuadTree[E](_, capacity));
      _entities = entities.filterNot(e => nodes.exists(_.insert(e)));
    }
    true;
  }
}