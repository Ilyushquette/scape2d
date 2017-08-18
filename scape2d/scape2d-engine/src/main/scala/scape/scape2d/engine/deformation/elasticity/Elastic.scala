package scape.scape2d.engine.deformation.elasticity

import com.google.common.math.DoubleMath._
import scape.scape2d.engine.deformation.LinearStressStrainGraph
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point

case class Elastic(graph:LinearStressStrainGraph, limit:Double) {
  def hardened(stress:Double) = {
    val elasticLine = Line(Point.origin, Point(limit, stress));
    val stiffness = elasticLine.slope.get;
    Elastic(LinearStressStrainGraph(stiffness), limit);
  }
  
  override def equals(any:Any) = any match {
    case Elastic(ograph, olimit) => graph == ograph && fuzzyEquals(limit, olimit, Epsilon);
    case _ => false;
  }
}