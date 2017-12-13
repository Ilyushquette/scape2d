package scape.scape2d.engine.core.matter

import org.junit.Assert
import org.junit.Test

import net.sf.cglib.proxy.Enhancer

class BondStructureTrackerProxyTest {
  @Test
  def testSettingDifferingDeformationDescriptorEventInterception = {
    var event:Option[StructureEvolutionEvent] = None;
    val bond = BondBuilder(ParticleBuilder().build, ParticleBuilder().build, 0).build;
    val structureTrackedBond = new BondStructureTrackerProxy(bond);
    structureTrackedBond.onStructureEvolution(e => event = Some(e));
    
    val old = structureTrackedBond.deformationDescriptor;
    val evolved = old.copy(
                      elastic = old.elastic.copy(
                          limit = 33));
    structureTrackedBond.setDeformationDescriptor(evolved);
    Assert.assertTrue(event.isDefined);
  }
  
  @Test
  def testSettingSameDeformationDescriptorNoEventInterception = {
    var event:Option[StructureEvolutionEvent] = None;
    val bond = BondBuilder(ParticleBuilder().build, ParticleBuilder().build, 0).build;
    val structureTrackedBond = new BondStructureTrackerProxy(bond);
    structureTrackedBond.onStructureEvolution(e => event = Some(e));
    
    structureTrackedBond.setDeformationDescriptor(structureTrackedBond.deformationDescriptor);
    Assert.assertTrue(event.isEmpty);
  }
  
  @Test
  def testBreakingDeformationDescriptorEventInterception = {
    var event:Option[StructureBreakEvent] = None;
    val bond = BondBuilder(ParticleBuilder().build, ParticleBuilder().build, 0).build;
    bond.particles._1.setBonds(Set(bond));
    bond.particles._2.setBonds(Set(bond));
    val structureTrackedBond = new BondStructureTrackerProxy(bond);
    structureTrackedBond.onStructureBreak(e => event = Some(e));
    
    structureTrackedBond.break();
    Assert.assertTrue(event.isDefined);
  }
  
  @Test
  def testReversedEnhancedVersion = {
    val bond = BondBuilder(ParticleBuilder().build, ParticleBuilder().build, 0).build;
    val structureTrackedBond = new BondStructureTrackerProxy(bond);
    
    val reversedStructureTrackedBond = structureTrackedBond.reversed;
    Assert.assertTrue(Enhancer.isEnhanced(reversedStructureTrackedBond.getClass));
  }
}