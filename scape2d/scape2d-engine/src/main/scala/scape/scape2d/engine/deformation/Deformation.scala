package scape.scape2d.engine.deformation

case class Deformation private[deformation](
  strain:Double,
  stress:Double,
  evolvedDescriptor:DeformationDescriptor);