package scape.scape2d.engine.util

import scala.collection.mutable.WeakHashMap
import scala.ref.WeakReference

class ProxyCache[T, P <: Proxy[T]](proxyFactory:T => P) {
  private val cache = WeakHashMap[T, WeakReference[P]]();
  
  def dump = cache.mapValues(_.get);
  
  def get(instance:T):P = {
    val weaklyReachableProxy = cache.get(instance);
    if(weaklyReachableProxy.isEmpty || weaklyReachableProxy.get.get.isEmpty) {
      val proxy = proxyFactory(instance);
      cache.put(instance, new WeakReference(proxy));
      cache.put(proxy.enhanced, new WeakReference(proxy));
      proxy;
    }else weaklyReachableProxy.get();
  }
}