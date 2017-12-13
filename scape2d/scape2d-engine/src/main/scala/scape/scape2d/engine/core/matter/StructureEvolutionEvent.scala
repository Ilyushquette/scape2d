package scape.scape2d.engine.core.matter

import scape.scape2d.engine.deformation.DeformationDescriptor

case class StructureEvolutionEvent(old:DeformationDescriptor, evolved:DeformationDescriptor);