package scape.scape2d.engine.deformation.plasticity

import java.lang.Math.abs

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.deformation.ShiftedStressStrainGraph
import scape.scape2d.engine.deformation.StressStrainGraph
import scape.scape2d.engine.geom.Epsilon

case class Plastic(graph:StressStrainGraph, limit:Double) {
  def strained(strain:Double) = {
    val absoluteStrain = abs(strain);
    if(absoluteStrain >= limit)
      throw new IllegalArgumentException("Strain %f occurs after fracture %f".format(absoluteStrain, limit));
    
    val shiftedGraph = graph match {
      case ShiftedStressStrainGraph(graph, permanentStrain) =>
        ShiftedStressStrainGraph(graph, permanentStrain + absoluteStrain);
      case graph => ShiftedStressStrainGraph(graph, absoluteStrain);
    }
    
    Plastic(shiftedGraph, limit - absoluteStrain);
  }
  
  override def equals(any:Any) = any match {
    case Plastic(ograph, olimit) => graph == ograph && fuzzyEquals(limit, olimit, Epsilon);
  }
}