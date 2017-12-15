package scape.scape2d.engine.geom

object Components2DInteger {
  implicit def widenComponents(componentsInteger:Components2DInteger):Components = {
    componentsInteger.componentsDouble;
  }
}

case class Components2DInteger(x:Int, y:Int) {
  lazy val componentsDouble = Components(x, y);
}