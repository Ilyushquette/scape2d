package scape.scape2d.engine.process

import net.sf.cglib.proxy.MethodProxy
import net.sf.cglib.proxy.MethodInterceptor
import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic
import net.sf.cglib.proxy.Enhancer
import scape.scape2d.engine.util.Entity
import scape.scape2d.engine.time.IoCDeferred
import java.util.concurrent.ConcurrentLinkedQueue

object ProcessOperationsDeferringProxy {
  def enhance[T <: Process](processProxy:ProcessOperationsDeferringProxy[T]):T = {
    val enhanced = Enhancer.create(processProxy.origin.getClass, processProxy);
    Entity.replicateFields(processProxy.origin, enhanced, field => !isStatic(field.getModifiers));
    enhanced.asInstanceOf[T];
  }
  
  implicit def autoEnhance[T <: Process](processProxy:ProcessOperationsDeferringProxy[T]) = processProxy.enhanced;
}

class ProcessOperationsDeferringProxy[T <: Process](val origin:T)
extends MethodInterceptor {
  lazy val enhanced = ProcessOperationsDeferringProxy.enhance(this);
  private val deferredMethods = {
    val methods = origin.getClass.getMethods;
    methods.filter(_ isAnnotationPresent classOf[IoCDeferred]);
  }
  private val queue = new ConcurrentLinkedQueue[() => Unit]();
  
  def intercept(origin:Object, method:Method, args:Array[Object], methodProxy:MethodProxy) = {
    if("integrate" equals method.getName) {
      val result = methodProxy.invokeSuper(origin, args);
      while(!queue.isEmpty) queue.poll()();
      result;
    }else if(deferredMethods contains method) {
      queue.offer(() => methodProxy.invokeSuper(origin, args));
      Nil;
    }else methodProxy.invokeSuper(origin, args);
  }
}