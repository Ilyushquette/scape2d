package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.CompositeShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.ShapeBounds
import scape.scape2d.engine.geom.shape.Shape

class ExpandedTree[E <: Formed[_ <: Shape]](
  val coreNode:Node[E],
  val nodeFactory:AxisAlignedRectangle => Node[E] = new Bucket[E](_),
  val expansion:Double
) extends Node[E] {
  val topLeft = AxisAlignedRectangle(Point(coreNode.bounds.topLeft.x - expansion, coreNode.bounds.topLeft.y), expansion, expansion);
  val topCenter = AxisAlignedRectangle(coreNode.bounds.topLeft, coreNode.bounds.width, expansion);
  val topRight = AxisAlignedRectangle(coreNode.bounds.topRight, expansion, expansion);
  val centerLeft = AxisAlignedRectangle(Point(topLeft.topLeft.x, coreNode.bounds.bottomLeft.y), expansion, coreNode.bounds.height);
  val centerRight = AxisAlignedRectangle(coreNode.bounds.bottomRight, expansion, coreNode.bounds.height);
  val bottomLeft = AxisAlignedRectangle(Point(topLeft.topLeft.x, coreNode.bounds.bottomLeft.y - expansion), expansion, expansion);
  val bottomCenter = AxisAlignedRectangle(bottomLeft.bottomRight, coreNode.bounds.width, expansion);
  val bottomRight = AxisAlignedRectangle(bottomCenter.bottomRight, expansion, expansion);
  
  val regions = List(topLeft, topCenter, topRight, centerLeft, centerRight, bottomLeft, bottomCenter, bottomRight);
  val bounds = ShapeBounds(CompositeShape(regions));
  val nodes = coreNode +: regions.map(nodeFactory);
  nodes.foreach(_.setParent(this));
  
  private var _parent:Option[Node[E]] = None;
  private var _entities:List[E] = List.empty;
  
  def parent = _parent;
  
  def setParent(newParent:Node[E]) = _parent = Some(newParent);
  
  def entities = _entities;
  
  def insert(entity:E) = {
    if(bounds.contains(entity.shape)){
      val inserted = nodes.exists(_ insert entity);
      if(!inserted) _entities = _entities :+ entity;
      true;
    }else false;
  }
}