package scape.scape2d.samples

import scape.scape2d.engine.core.Nature
import scape.scape2d.engine.geom.Point2D
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.matter.Particle

object NewtonFirstLaw {
  def main(args:Array[String]):Unit = {
    val nature = new Nature(60);
    val metalParticle = new Particle(Point2D.origin, 5, 2, new Vector2D(2, 45));
    nature.addParticle(metalParticle);
    nature.start;
  }
}