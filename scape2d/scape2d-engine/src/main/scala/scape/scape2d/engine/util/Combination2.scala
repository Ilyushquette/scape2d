package scape.scape2d.engine.util

case class Combination2[T1, T2](_1:T1, _2:T2) {
  lazy val reversed = Combination2(_2, _1);
  
  override def hashCode = _1.hashCode + _2.hashCode;
  
  override def equals(any:Any) = any match {
    case Combination2(o_1, o_2) => (_1 == o_1 && _2 == o_2) ||
                                   (_1 == o_2 && _2 == o_1);
    case _ => false;
  }
}