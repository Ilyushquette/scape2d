package scape.scape2d.engine.util

sealed trait Solution[+A] {
  def hasSolution:Boolean;
  
  def hasNoSolution:Boolean;
  
  def hasInfiniteSolutions:Boolean;
  
  def solution:A;
  
  def solutionOrElse[B >: A](default: => B):B = if(hasNoSolution || hasInfiniteSolutions) default else solution;
  
  def map[B](fn:A => B):Solution[B] = {
    if(hasSolution) SomeSolution(fn(solution));
    else if(hasNoSolution) NoSolution;
    else InfiniteSolutions;
  }
}

case class SomeSolution[A](solution:A) extends Solution[A] {
  def hasSolution = true;
  
  def hasNoSolution = false;
  
  def hasInfiniteSolutions = false;
}

case object NoSolution extends Solution[Nothing] {
  def hasSolution = false;
  
  def hasNoSolution = true;
  
  def hasInfiniteSolutions = false;
  
  def solution = throw new NoSuchElementException("NoSolution.solution");
}

case object InfiniteSolutions extends Solution[Nothing] {
  def hasSolution = false;
  
  def hasNoSolution = false;
  
  def hasInfiniteSolutions = true;
  
  def solution = throw new NoSuchElementException("InfiniteSolutions.solution");
}