package scape.scape2d.engine.process

import org.junit.Test
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.doubleToTime
import org.junit.Assert

class ProcessOperationsDeferringProxyTest {
  @Test
  def testOperationsNotMarkedDeferredProcessedRightAway = {
    val process = new ProcessCalculatorMock(100);
    val processProxy = new ProcessOperationsDeferringProxy(process);
    processProxy / 5;
    processProxy.integrate(15(Millisecond));
    Assert.assertEquals(35, processProxy.counter);
  }
  
  @Test
  def testOperationsMarkedDeferredProcessedAfterIntegration = {
    val process = new ProcessCalculatorMock(100);
    val processProxy = new ProcessOperationsDeferringProxy(process);
    processProxy * 5;
    processProxy.integrate(11(Millisecond));
    Assert.assertEquals(555, processProxy.counter);
  }
}