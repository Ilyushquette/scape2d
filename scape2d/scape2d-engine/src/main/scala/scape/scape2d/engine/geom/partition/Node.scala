package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle

trait Node[E <: Formed[_ <: Shape]] {
  def bounds:AxisAlignedRectangle;
  
  def entities:List[E];
  
  def subEntities:List[E] = entities ++ nodes.flatMap(_.subEntities);
  
  def nodes:List[Node[E]];
  
  def insert(entity:E):Boolean;
}