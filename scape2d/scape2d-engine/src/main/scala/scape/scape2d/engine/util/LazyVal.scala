package scape.scape2d.engine.util

object LazyVal {
  def apply[T](eval: => T) = new LazyVal(eval);
  
  implicit def autoExpose[T](lazyVal:LazyVal[T]) = lazyVal.value;
}

class LazyVal[T](eval: => T) {
  lazy val value = eval;
}