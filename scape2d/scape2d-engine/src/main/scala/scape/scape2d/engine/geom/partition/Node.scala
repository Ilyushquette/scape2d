package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle

trait Node[E <: Formed[_ <: Shape]] {
  def bounds:AxisAlignedRectangle;
  
  def entities:List[E];
  
  def superEntities:List[E] = parent.flatten(p => p.entities ++ p.superEntities).toList;
  
  def subEntities:List[E] = nodes.flatMap(n => n.entities ++ n.subEntities);
  
  def parent:Option[Node[E]];
  
  def setParent(newParent:Node[E]);
  
  def nodes:List[Node[E]];
  
  def findTreeNode(entity:E):Option[Node[E]] = if(entities contains entity) Some(this);
                                               else findNode(entity, nodes);
  
  def insert(entity:E):Boolean;
}