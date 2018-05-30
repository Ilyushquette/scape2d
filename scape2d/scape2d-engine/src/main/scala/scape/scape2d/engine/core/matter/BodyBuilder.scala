package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.fetchWaypoints
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.structure.SegmentedStructure
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.motion.rotational.AngularVelocity

case class BodyBuilder(
  particleFactory:Point => Particle = point => ParticleBuilder().as(Circle(point, 1)).build,
  bondFactory:(Particle, Particle) => Bond = BondBuilder(_, _).build,
  angularVelocity:AngularVelocity = AngularVelocity.zero,
  torque:Double = 0
) {
  def withParticleFactory(pf:Point => Particle) = copy(particleFactory = pf);
  
  def withBondFactory(bf:(Particle, Particle) => Bond) = copy(bondFactory = bf);
  
  def withAngularVelocity(av:AngularVelocity) = copy(angularVelocity = av);
  
  def withTorque(t:Double) = copy(torque = t);
  
  def build(fixedPoint:Point, structure:SegmentedStructure) = {
    val points = fetchWaypoints(structure.segments.iterator);
    val particles = points.map(particleFactory(_));
    val centerOfMass = particles.find(_.position == fixedPoint).get;
    val bonds = makeBonds(structure.segments, particles);
    val body = new Body(centerOfMass, bonds.toSet, angularVelocity, torque);
    bonds.foreach(_.setBody(Some(body)));
    body;
  }
  
  private def makeBonds(segments:List[Segment], particles:Set[Particle], bonds:List[Bond] = List()):List[Bond] = {
    segments match {
      case s::Nil => bonds ++ makeOriginalAndReversedBond(s, particles);
      case s::ss => makeBonds(ss, particles, bonds ++ makeOriginalAndReversedBond(s, particles));
      case Nil => throw new IllegalArgumentException("Couldn't create body without structure");
    }
  }
  
  private def makeOriginalAndReversedBond(segment:Segment, particles:Set[Particle]) = {
    val particle1 = particles.find(_.position == segment.p1).get;
    val particle2 = particles.find(_.position == segment.p2).get;
    val bond = bondFactory(particle1, particle2);
    val reversedBond = particle2.bonds.find(_.particles == Combination2(particle1, particle2)).get;
    List(bond, reversedBond);
  }
}