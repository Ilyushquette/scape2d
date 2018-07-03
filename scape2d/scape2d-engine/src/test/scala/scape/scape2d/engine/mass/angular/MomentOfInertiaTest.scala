package scape.scape2d.engine.mass.angular

import org.junit.Test
import org.junit.Assert

import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.mass.Kilogram

class MomentOfInertiaTest {
  @Test
  def testMomentsOfInertiaNotEqual = {
    Assert.assertFalse(MomentOfInertia(1(Kilogram), 3) == MomentOfInertia(3(Kilogram), 1));
  }
  
  @Test
  def testMomentsOfInertiaEqual = {
    Assert.assertTrue(MomentOfInertia(25(Kilogram), 2) == MomentOfInertia(4(Kilogram), 5));
  }
}