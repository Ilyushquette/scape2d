package scape.scape2d.engine.util

package object comparison {
  def min[T <: Comparable[T]](comparable1:T, comparable2:T) = {
    if((comparable1 compareTo comparable2) <= 0) comparable1 else comparable2;
  }
  
  def min[T <: Comparable[T]](comparables:T*) = comparables.min;
  
  def max[T <: Comparable[T]](comparable1:T, comparable2:T) = {
    if((comparable1 compareTo comparable2) >= 0) comparable1 else comparable2;
  }
  
  def max[T <: Comparable[T]](comparables:T*) = comparables.max;
}