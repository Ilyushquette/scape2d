package scape.scape2d.engine.motion

import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Point2D

object MovableTrackerProxy {
  private def enhance[T <: Movable](interceptor:MovableTrackerProxy[T]) = {
    val delegateClass = interceptor.delegate.getClass;
    val enhanced = Enhancer.create(delegateClass, interceptor);
    delegateClass.getDeclaredFields.foreach(field => if(!isStatic(field.getModifiers)) {
      field.setAccessible(true);
      field.set(enhanced, field.get(interceptor.delegate));
    });
    enhanced.asInstanceOf[T];
  }
  
  implicit def autoEnhance[T <: Movable](proxy:MovableTrackerProxy[T]) = proxy.enhanced;
}

class MovableTrackerProxy[T <: Movable](private val delegate:T) extends MethodInterceptor {
  private var motionListeners:Set[(Point2D, T) => Unit] = Set();
  lazy val enhanced = MovableTrackerProxy.enhance(this);
  
  def addMotionListener(listener:(Point2D, T) => Unit) = motionListeners = motionListeners + listener;
  
  def removeMotionListener(listener:(Point2D, T) => Unit) = motionListeners = motionListeners - listener;
  
  def intercept(obj:Object, method:Method, args:Array[Object], methodProxy:MethodProxy) = {
    if("setPosition" == method.getName()) (obj, args) match {
      case (proxy:T, Array(nextPosition:Point2D)) =>
        val oldPosition = proxy.position;
        methodProxy.invokeSuper(obj, args);
        motionListeners.foreach(_(oldPosition, proxy));
        Nil;
      case _ => methodProxy.invokeSuper(obj, args);
    }else methodProxy.invokeSuper(obj, args);
  }
}