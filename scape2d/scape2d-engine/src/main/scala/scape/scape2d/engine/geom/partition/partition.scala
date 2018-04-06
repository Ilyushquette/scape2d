package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Shape

package object partition {
  def findNode[E <: Formed[_ <: Shape]](entity:E, nodes:List[Node[E]]):Option[Node[E]] = nodes match {
    case Nil => None;
    case n::ns =>
      val nodeOpt = n.findTreeNode(entity);
      if(nodeOpt.isEmpty) findNode(entity, ns);
      else nodeOpt;
  }
}