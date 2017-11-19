package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.fetchWaypoints
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.structure.SegmentedStructure
import scape.scape2d.engine.geom.shape.Segment

case class BodyBuilder(
  particleFactory:Point => Particle = point => ParticleBuilder().as(Circle(point, 1)).build,
  bondFactory:(Particle, Particle) => Bond = BondBuilder(_, _).build,
  angularVelocity:Double = 0
) {
  def withParticleFactory(pf:Point => Particle) = copy(particleFactory = pf);
  
  def withBondFactory(bf:(Particle, Particle) => Bond) = copy(bondFactory = bf);
  
  def withAngularVelocity(av:Double) = copy(angularVelocity = av);
  
  def build(fixedPoint:Point, structure:SegmentedStructure) = {
    val points = fetchWaypoints(structure.segments.iterator);
    val centerOfMass = particleFactory(fixedPoint);
    val particles = points.map(particleFactory(_));
    val bonds = makeBonds(structure.segments, particles + centerOfMass);
    val body = new Body(centerOfMass, bonds, angularVelocity);
    bonds.foreach(_.setBody(Some(body)));
    body;
  }
  
  private def makeBonds(segments:List[Segment], particles:Set[Particle], bonds:Set[Bond] = Set()):Set[Bond] = {
    segments match {
      case s::Nil => bonds + makeBond(s, particles);
      case s::ss => makeBonds(ss, particles, bonds + makeBond(s, particles));
      case Nil => throw new IllegalArgumentException("Couldn't create body without structure");
    }
  }
  
  private def makeBond(segment:Segment, particles:Set[Particle]) = {
    val particle1 = particles.find(_.shape.center == segment.p1).get;
    val particle2 = particles.find(_.shape.center == segment.p2).get;
    bondFactory(particle1, particle2);
  }
}