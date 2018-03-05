package scape.scape2d.engine.geom.partition

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.Formed
import scala.collection.mutable.HashMap
import scape.scape2d.engine.core.Identifiable

class QuadTree[E <: Formed[_ <: Shape] with Identifiable] private[QuadTree](
  private var _parent:Option[Node[E]],
  val bounds:AxisAlignedRectangle,
  val capacity:Int,
  private val hashtable:HashMap[Any, Node[E]]
) extends Node[E] {
  private var _entities:List[E] = List.empty;
  private var _nodes:List[QuadTree[E]] = List.empty;
  
  def this(bounds:AxisAlignedRectangle, capacity:Int) = {
    this(None, bounds, capacity, HashMap());
  }
  
  def parent = _parent;
  
  def setParent(newParent:Node[E]) = _parent = Some(newParent);
  
  def entities = _entities;
  
  def nodes = _nodes;
  
  override def findTreeNode(entity:E) = hashtable.get(entity.id);
  
  def insert(entity:E):Boolean = {
    if(bounds.contains(entity.shape)) {
      val inserted = nodes.exists(_ insert entity);
      if(!inserted) insertHere(entity);
      true;
    }else false;
  }
  
  private def insertHere(entity:E) = {
    _entities = _entities :+ entity;
    hashtable.put(entity.id, this);
    if(entities.size > capacity && nodes.isEmpty) {
      val dimensions = bounds.slice(4);
      _nodes = dimensions.map(new QuadTree[E](Some(this), _, capacity, hashtable)).toList;
      _entities = entities.filterNot(e => nodes.exists(_.insert(e)));
    }
  }
}