package scape.scape2d.engine.util

import java.lang.reflect.Field

object Entity {
  def replicateFields(origin:Any, destination:Any, predicate:Field => Boolean = _ => true) = {
    val fields = origin.getClass.getDeclaredFields.filter(predicate);
    fields.foreach(replicateField(origin, destination, _));
  }
  
  def replicateField(origin:Any, destination:Any, field:Field) = {
    field.setAccessible(true);
    field.set(destination, field.get(origin));
  }
}
