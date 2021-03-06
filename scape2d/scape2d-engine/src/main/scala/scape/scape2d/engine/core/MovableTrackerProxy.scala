package scape.scape2d.engine.core

import scala.collection.mutable.HashSet

import net.sf.cglib.proxy.MethodProxy
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.util.Proxy
import scape.scape2d.engine.util.ProxyCache

object MovableTrackerProxy {
  val cache = new ProxyCache[Movable[_ <: Shape], MovableTrackerProxy[Movable[_ <: Shape]]](new MovableTrackerProxy(_));
  
  def track[T <: Movable[_ <: Shape]](movable:T):MovableTrackerProxy[T] = {
    cache.get(movable).asInstanceOf[MovableTrackerProxy[T]];
  }
}

class MovableTrackerProxy[T <: Movable[_ <: Shape]] private[MovableTrackerProxy](val origin:T)
extends Proxy[T] {
  private val motionListeners = HashSet[MotionEvent[T] => Unit]();
  
  def onMotion(listener:MotionEvent[T] => Unit) = motionListeners += listener;
  
  def offMotion(listener:MotionEvent[T] => Unit) = motionListeners -= listener;
  
  def offMotion() = motionListeners.clear();
  
  def handle(origin:T, methodProxy:MethodProxy) = {
    case ("setShape", (nextShape:Shape)::Nil) =>
      val old = origin.snapshot.asInstanceOf[T];
      val result = methodProxy.invokeSuper(origin, Array(nextShape));
      if(old.shape != nextShape) motionListeners.foreach(_(MotionEvent(old, origin)));
      result;
    case (_, args) =>
      methodProxy.invokeSuper(origin, args.toArray);
  }
}