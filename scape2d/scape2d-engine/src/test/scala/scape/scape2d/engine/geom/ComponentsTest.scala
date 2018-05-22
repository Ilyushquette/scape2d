package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class ComponentsTest {
  @Test
  def testSameComponents = Assert.assertTrue(Components(-5, 2) == Components(-5, 2));
  
  @Test
  def testDifferentComponentsByX = Assert.assertFalse(Components(3, 3) == Components(2.9, 3));
  
  @Test
  def testDifferentComponentsByY = Assert.assertFalse(Components(13.26, 17.33) == Components(13.26, -17.33));
  
  @Test
  def testDifferentComponents = Assert.assertFalse(Components(4, 6) == Components(5, 2));
}