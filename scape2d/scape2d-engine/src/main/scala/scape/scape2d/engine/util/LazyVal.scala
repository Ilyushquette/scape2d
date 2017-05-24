package scape.scape2d.engine.util

object LazyVal {
  def apply[T](eval: => T) = new LazyVal(eval);
}

class LazyVal[T](eval: => T) {
  lazy val value = eval;
}