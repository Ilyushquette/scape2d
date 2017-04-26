package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

class Vector2DTest {
  @Test
  def testEqualsSame = Assert.assertEquals(new Vector2D(3, 55.5), new Vector2D(3, 55.5));
  
  @Test
  def testEqualsNotSame = Assert.assertNotSame(new Vector2D(78, 13.23), new Vector2D(77, 13.23));
}