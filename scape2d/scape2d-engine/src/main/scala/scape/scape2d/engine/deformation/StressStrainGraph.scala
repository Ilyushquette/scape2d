package scape.scape2d.engine.deformation

trait StressStrainGraph {
  /**
   * strain in meters, stress in Newtons
   */
  def forStrain(strain:Double):Double;
  
  /**
   * stress in Newtons, strain in meters
   */
  def forStress(stress:Double):Double;
}