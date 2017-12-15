package scape.scape2d.engine.geom

object Components2DInteger {
  implicit def widenComponents(componentsInteger:ComponentsInteger):Components = {
    componentsInteger.componentsDouble;
  }
}

case class ComponentsInteger(x:Int, y:Int) {
  lazy val componentsDouble = Components(x, y);
}
