package scape.scape2d.engine.core

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.gravity.gravitationalForceMagnitudeBetween
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.mass.angular.MomentOfInertia

trait Matter[T <: FiniteShape] extends Movable[T] {
  def mass:Mass;
  
  def rotatable:Option[Rotatable];
  
  def momentOfInertia:MomentOfInertia;
  
  def gravitationalForceOnto(matter:Matter[_ <: FiniteShape]) = {
    Vector(gravitationalForceMagnitudeBetween(this, matter), matter.position angleTo this.position);
  }
}