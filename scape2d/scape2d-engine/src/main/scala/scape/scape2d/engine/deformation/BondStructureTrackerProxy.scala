package scape.scape2d.engine.deformation

import java.lang.reflect.Method
import java.lang.reflect.Modifier.isStatic

import scala.collection.mutable.HashSet

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import scape.scape2d.engine.core.matter.Bond

object BondStructureTrackerProxy {
  private def enhance(interceptor:BondStructureTrackerProxy) = {
    val bondClass = classOf[Bond];
    val enhanced = Enhancer.create(bondClass, interceptor);
    bondClass.getDeclaredFields.foreach(field => if(!isStatic(field.getModifiers)) {
      field.setAccessible(true);
      field.set(enhanced, field.get(interceptor.delegate));
    });
    enhanced.asInstanceOf[Bond];
  }
  
  implicit def autoEnhance(proxy:BondStructureTrackerProxy) = proxy.enhanced;
}

class BondStructureTrackerProxy(val delegate:Bond) extends MethodInterceptor {
  private var structureEvolutionListeners = new HashSet[StructureEvolutionEvent => Unit]();
  private var structureBreakListeners = new HashSet[StructureBreakEvent => Unit]();
  lazy val enhanced = BondStructureTrackerProxy.enhance(this);
  
  def addStructureEvolutionListener(listener:StructureEvolutionEvent => Unit) = {
    structureEvolutionListeners.add(listener);
  }
  
  def removeStructureEvolutionListener(listener:StructureEvolutionEvent => Unit) = {
    structureEvolutionListeners.remove(listener);
  }
  
  def addStructureBreakListener(listener:StructureBreakEvent => Unit) = {
    structureBreakListeners.add(listener);
  }
  
  def removeStructureBreakListener(listener:StructureBreakEvent => Unit) = {
    structureBreakListeners.remove(listener);
  }
  
  def intercept(obj:Object, method:Method, args:Array[Object], methodProxy:MethodProxy) = {
    val methodName = method.getName();
    if("setDeformationDescriptor" == methodName) args match {
      case Array(evolved:DeformationDescriptor) =>
        handleStructureEvolution(obj.asInstanceOf[Bond], methodProxy, evolved);
      case _ =>
        methodProxy.invokeSuper(obj, args);
    }else if("break" == methodName) {
      handleStructureBreak(obj.asInstanceOf[Bond], methodProxy, args);
    }else if("reversed" == methodName) args match {
      case Array() =>
        reverseAndEnhanceBond(obj.asInstanceOf[Bond], methodProxy);
      case _ =>
        methodProxy.invokeSuper(obj, args);
    }else methodProxy.invokeSuper(obj, args);
  }
  
  private def handleStructureEvolution(proxy:Bond, methodProxy:MethodProxy, evolved:DeformationDescriptor) = {
    val old = proxy.deformationDescriptor;
    val result = methodProxy.invokeSuper(proxy, Array(evolved));
    if(old != evolved)
      structureEvolutionListeners.foreach(_(StructureEvolutionEvent(old, evolved)));
    result;
  }
  
  private def handleStructureBreak(proxy:Bond, methodProxy:MethodProxy, args:Array[Object]) = {
    val result = methodProxy.invokeSuper(proxy, args);
    structureBreakListeners.foreach(_(StructureBreakEvent(proxy)));
    result;
  }
  
  private def reverseAndEnhanceBond(proxy:Bond, methodProxy:MethodProxy) = {
    val reversedBond = methodProxy.invokeSuper(proxy, Array.empty).asInstanceOf[Bond];
    val structureTrackedReversedBond = new BondStructureTrackerProxy(reversedBond);
    structureTrackedReversedBond.structureEvolutionListeners = structureEvolutionListeners;
    structureTrackedReversedBond.structureBreakListeners = structureBreakListeners;
    structureTrackedReversedBond.enhanced;
  }
}