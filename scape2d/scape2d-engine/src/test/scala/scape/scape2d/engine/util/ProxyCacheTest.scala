package scape.scape2d.engine.util

import net.sf.cglib.proxy.MethodProxy
import org.junit.Test
import org.junit.Assert
import scala.ref.WeakReference

object ProxyCacheTest {
  private class Mock;
  
  private class MockProxy(val origin:Mock) extends Proxy[Mock] {
    def handle(origin:Mock, methodProxy:MethodProxy) = {
      case (_, args) => methodProxy.invokeSuper(origin, args.toArray);
    }
  }
}

class ProxyCacheTest {
  import ProxyCacheTest.{Mock, MockProxy}
  
  @Test
  def testProxyCached = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    val proxy = cache.get(mock);
    val expectedDump = Map(mock -> Some(proxy), proxy.enhanced -> Some(proxy));
    Assert.assertEquals(expectedDump, cache.dump);
  }
  
  @Test
  def testProxyResolvedFromCacheByOriginalInstance = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    val proxy = cache.get(mock);
    val subsequentProxy = cache.get(mock);
    Assert.assertTrue(proxy eq subsequentProxy);
  }
  
  @Test
  def testProxyResolvedFromCacheByEnhancedInstance = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    val proxy = cache.get(mock);
    val subsequentProxy = cache.get(proxy.enhanced);
    Assert.assertTrue(proxy eq subsequentProxy);
  }
  
  @Test
  def testProxyEnhancedCacheEntryGarbageCollectedIfEnhancedReferenceLost = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    var proxy = cache.get(mock);
    proxy = null;
    System.gc();
    Thread.sleep(100);
    Assert.assertFalse(cache.dump.exists({case (k, _) => k != mock}));
  }
  
  @Test
  def testProxyOriginalCacheEntryValueGarbageCollectedIfEnhancedReferenceLost = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    var proxy = cache.get(mock);
    proxy = null;
    System.gc();
    Thread.sleep(100);
    val expectedDump = Map(mock -> None);
    Assert.assertEquals(expectedDump, cache.dump);
  }
  
  @Test
  def testProxyRecreationAfterGarbageCollection = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    var proxy = cache.get(mock);
    proxy = null;
    System.gc();
    Thread.sleep(100);
    proxy = cache.get(mock);
    val expectedDump = Map(mock -> Some(proxy), proxy.enhanced -> Some(proxy));
    Assert.assertEquals(expectedDump, cache.dump);
  }
  
  @Test
  def testProxyCacheEntryNotGarbageCollectedIfProxyReferenceExists = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    var proxy = cache.get(mock);
    System.gc();
    Thread.sleep(100);
    Assert.assertFalse(cache.dump.isEmpty);
  }
  
  @Test
  def testProxyCacheEntryNotGarbageCollectedIfEnhancedReferenceExists = {
    val mock = new Mock();
    val cache = new ProxyCache[Mock, MockProxy](new MockProxy(_));
    var enhanced = cache.get(mock).enhanced;
    System.gc();
    Thread.sleep(100);
    Assert.assertFalse(cache.dump.isEmpty);
  }
}