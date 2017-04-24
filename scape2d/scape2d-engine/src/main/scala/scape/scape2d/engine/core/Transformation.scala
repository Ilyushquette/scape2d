package scape.scape2d.engine.core
import scape.scape2d.engine.matter.Particle
import scala.collection.mutable.LinkedHashSet
import scape.scape2d.engine.matter.Event

class Transformation[engine] {
  private[engine] val transformers = new LinkedHashSet[Particle => List[Event]];
  
  def +=(transformer:Particle => List[Event]) = transformers.add(transformer);
  
  def -=(transformer:Particle => List[Event]) = transformers.remove(transformer);
  
  private[engine] def apply(particle:Particle) = transformers.flatMap(_(particle));
}