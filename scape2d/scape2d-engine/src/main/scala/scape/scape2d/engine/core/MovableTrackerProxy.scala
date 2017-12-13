package scape.scape2d.engine.core

import net.sf.cglib.proxy.MethodProxy
import scape.scape2d.engine.util.Proxy
import scala.collection.mutable.HashSet
import scape.scape2d.engine.geom.shape.Point

class MovableTrackerProxy[T <: Movable](val origin:T)
extends Proxy[T] {
  private val motionListeners = HashSet[MotionEvent[T] => Unit]();
  
  def onMotion(listener:MotionEvent[T] => Unit) = motionListeners += listener;
  
  def offMotion(listener:MotionEvent[T] => Unit) = motionListeners -= listener;
  
  def offMotion() = motionListeners.clear();
  
  def handle(origin:T, methodProxy:MethodProxy) = {
    case ("setPosition", (nextPosition:Point)::Nil) =>
      val old = origin.snapshot.asInstanceOf[T];
      val result = methodProxy.invokeSuper(origin, Array(nextPosition));
      if(old.position != nextPosition) motionListeners.foreach(_(MotionEvent(old, origin)));
      result;
    case (_, args) =>
      methodProxy.invokeSuper(origin, args.toArray);
  }
}