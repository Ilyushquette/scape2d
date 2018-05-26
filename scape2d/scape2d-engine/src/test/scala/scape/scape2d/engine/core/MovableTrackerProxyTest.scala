package scape.scape2d.engine.core

import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.Vector

class MovableTrackerProxyTest {
  @Test
  def testSettingDifferingPositionEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), Vector.zero, None);
    val trackedMovable = MovableTrackerProxy.track(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setPosition(Point(4, 5));
    Assert.assertTrue(event.isDefined);
  }
  
  @Test
  def testSettingSamePositionNoEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), Vector.zero, None);
    val trackedMovable = MovableTrackerProxy.track(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setPosition(Point(5, 5));
    Assert.assertTrue(event.isEmpty);
  }
}