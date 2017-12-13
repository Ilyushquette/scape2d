package scape.scape2d.engine.core

import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.Vector2D

object MovableTrackerProxyTest {
  private class MovableMock(var position:Point, var velocity:Vector2D) extends Movable {
    def this() = this(Point.origin, new Vector2D());
    
    def setPosition(nextPosition:Point) = position = nextPosition;
    def setVelocity(newVelocity:Vector2D) = velocity = newVelocity;
    def rotatable = None;
    def snapshot = new MovableMock(position, velocity);
  }
}

class MovableTrackerProxyTest {
  import MovableTrackerProxyTest.MovableMock
  
  @Test
  def testSettingDifferingPositionEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), new Vector2D());
    val trackedMovable = new MovableTrackerProxy(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setPosition(Point(4, 5));
    Assert.assertTrue(event.isDefined);
  }
  
  @Test
  def testSettingSamePositionNoEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), new Vector2D());
    val trackedMovable = new MovableTrackerProxy(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setPosition(Point(5, 5));
    Assert.assertTrue(event.isEmpty);
  }
}