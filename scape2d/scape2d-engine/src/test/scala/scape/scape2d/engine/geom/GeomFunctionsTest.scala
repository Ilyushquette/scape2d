package scape.scape2d.engine.geom

import org.junit.Test
import org.junit.Assert

class TestGeomFunctions {
  @Test
  def testCalculateComponents() = {
    val vector = new Vector2D(7, 15);
    Assert.assertEquals(Components2D(6.761480784023478, 1.8117333157176452), calculateComponents(vector));
  }
}