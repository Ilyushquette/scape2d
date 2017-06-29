package scape.scape2d.engine.motion.collision.detection

import scala.collection.Iterable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Sweepable
import scala.collection.mutable.HashSet

class QuadTreeBasedCollisionDetector[T <: Movable[T] with Formed[_ <: Sweepable[_]]](
  val bounds:AxisAlignedRectangle,
  val detect:(T, T, Double) => Option[Double]
) extends CollisionDetector[T] {
  type Tree = QuadTree[SweepFormingMovable[T]];
  private val treeCreationListeners = HashSet[Tree => Unit]();
  
  def detect(entities:Iterable[T], timestep:Double) = {
    val tree = new QuadTree[SweepFormingMovable[T]](bounds, 4);
    entities.foreach(e => tree.insert(SweepFormingMovable[T](e, timestep)));
    treeCreationListeners.foreach(_(tree));
    Iterator.empty;
  }
  
  def addTreeCreationListener(listener:Tree => Unit) = treeCreationListeners += listener;
  
  def removeTreeCreationListener(listener:Tree => Unit) = treeCreationListeners -= listener;
}