package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle

class Bucket[E <: Formed[_ <: Shape]](
  val bounds:AxisAlignedRectangle
) extends Node[E] {
  val nodes = List.empty;
  private var _entities:List[E] = List.empty;
  
  def entities = _entities;
  
  def insert(entity:E) = {
    if(bounds.contains(entity.shape)){
      _entities = _entities :+ entity;
      true;
    }else false;
  }
}