package scape.scape2d.engine.core.matter

import scala.collection.mutable.HashSet

import net.sf.cglib.proxy.MethodProxy
import scape.scape2d.engine.deformation.DeformationDescriptor
import scape.scape2d.engine.util.Proxy

object BondStructureTrackerProxy {  
  def track(origin:Bond) = {
    val proxy = new BondStructureTrackerProxy(origin);
    // recursive reversed proxy construction
    proxy.particles._2.setBonds(proxy.particles._2.bonds - origin.reversed + proxy.reversed);
    proxy;
  }
}

class BondStructureTrackerProxy private[BondStructureTrackerProxy](
  val origin:Bond,
  private val structureEvolutionListeners:HashSet[StructureEvolutionEvent => Unit] = HashSet(),
  private val structureBreakListeners:HashSet[StructureBreakEvent => Unit] = HashSet())
extends Proxy[Bond] {
  enhanced.particles._1.setBonds(enhanced.particles._1.bonds - origin + enhanced);
  
  def onStructureEvolution(listener:StructureEvolutionEvent => Unit) = structureEvolutionListeners += listener;
  
  def offStructureEvolution(listener:StructureEvolutionEvent => Unit) = structureEvolutionListeners -= listener;
  
  def offStructureEvolution() = structureEvolutionListeners.clear();
  
  def onStructureBreak(listener:StructureBreakEvent => Unit) = structureBreakListeners += listener;
  
  def offStructureBreak(listener:StructureBreakEvent => Unit) = structureBreakListeners -= listener;
  
  def offStructureBreak() = structureBreakListeners.clear();
  
  def handle(origin:Bond, methodProxy:MethodProxy) = {
    case ("setDeformationDescriptor", (evolved:DeformationDescriptor)::Nil) =>
      val old = origin.deformationDescriptor;
      val result = methodProxy.invokeSuper(origin, Array(evolved));
      if(old != evolved) structureEvolutionListeners.foreach(_(StructureEvolutionEvent(old, evolved)));
      result;
    case ("break", Nil) =>
      val result = methodProxy.invokeSuper(origin, Array.empty);
      structureBreakListeners.foreach(_(StructureBreakEvent(origin)));
      result;
    case ("reversed", Nil) =>
      val reversedBond = methodProxy.invokeSuper(origin, Array.empty).asInstanceOf[Bond];
      // non-recursive reversed proxy construction
      new BondStructureTrackerProxy(reversedBond, structureEvolutionListeners, structureBreakListeners).enhanced;
    case (_, args) =>
      methodProxy.invokeSuper(origin, args.toArray);
  }
}