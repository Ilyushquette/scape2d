package scape.scape2d.engine.util

import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy

object Proxy {
  def enhance[T](proxy:Proxy[T]):T = {
    val enhanced = Enhancer.create(proxy.origin.getClass, proxy);
    Entity.replicateFields(proxy.origin, enhanced, field => !isStatic(field.getModifiers));
    enhanced.asInstanceOf[T];
  }
  
  implicit def autoEnhance[T](proxy:Proxy[T]) = proxy.enhanced;
}

trait Proxy[T] extends MethodInterceptor {
  lazy val enhanced = Proxy.enhance(this);
  
  final def intercept(origin:Object, method:Method, args:Array[Object], methodProxy:MethodProxy) = {
    handle(origin.asInstanceOf[T], methodProxy)(method.getName, args.toList);
  }
  
  def handle(origin:T, methodProxy:MethodProxy):PartialFunction[(String, List[Object]), Object];
  
  def origin:T;
}