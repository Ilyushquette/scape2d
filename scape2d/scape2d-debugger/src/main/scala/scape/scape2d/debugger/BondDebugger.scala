package scape.scape2d.debugger

import java.lang.Math.abs
import java.lang.Math.signum
import scape.scape2d.debugger.view.GraphView
import scape.scape2d.engine.core.matter.BondStructureTrackerProxy
import scape.scape2d.engine.deformation.DeformationDescriptor
import scape.scape2d.debugger.view.ParticleTrackingView

class BondDebugger(val particleTrackingView:ParticleTrackingView, val graphViewFactory:() => GraphView) {
  def trackStructureEvolution(structureTrackedBond:BondStructureTrackerProxy) = {
    val graphView = graphViewFactory();
    val initial = structureTrackedBond.deformationDescriptor;
    graphView.render(fxOf(initial), 0d to initial.plastic.limit by 0.005);
    
    structureTrackedBond.onStructureEvolution(event => {
      graphView.clear(fxOf(event.old), 0d to event.old.plastic.limit by 0.005);
      graphView.render(fxOf(event.evolved), 0d to event.evolved.plastic.limit by 0.005);
    });
    
    structureTrackedBond.onStructureBreak(event => particleTrackingView.clearBond(event.broken));
  }
  
  private def fxOf(deformationDescriptor:DeformationDescriptor) = (x:Double) => {
    if(x <= deformationDescriptor.elastic.limit)
      deformationDescriptor.elastic.graph.forStrain(x);
    else
      deformationDescriptor.plastic.graph.forStrain(x);
  }
}