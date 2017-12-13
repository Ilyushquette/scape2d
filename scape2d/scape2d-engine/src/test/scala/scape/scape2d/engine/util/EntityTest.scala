package scape.scape2d.engine.util

import org.junit.Test
import org.junit.Assert

class EntityTest {
  @Test
  def testReplicateField = {
    val origin = Bean(32, "something");
    val destination = Bean(16, "something2");
    Entity.replicateField(origin, destination, classOf[Bean].getDeclaredField("name"));
    Assert.assertEquals(Bean(16, "something"), destination);
  }
  
  @Test
  def testReplicateFields = {
    val origin = Bean(32, "something");
    val destination = Bean(16, "something2");
    Entity.replicateFields(origin, destination);
    Assert.assertEquals(Bean(32, "something"), destination);
  }
  
  @Test
  def testReplicateFieldsFilteredByName = {
    val origin = Bean(32, "something");
    val destination = Bean(16, "something2");
    Entity.replicateFields(origin, destination, _.getName().equals("id"));
    Assert.assertEquals(Bean(32, "something2"), destination);
  }
  
  private case class Bean(id:Long, name:String);
}