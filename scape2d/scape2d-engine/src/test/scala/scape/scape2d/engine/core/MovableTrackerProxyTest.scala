package scape.scape2d.engine.core

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.util.Proxy.autoEnhance
import scape.scape2d.engine.core.mock.MovableMock

class MovableTrackerProxyTest {
  @Test
  def testSettingNewPositionEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), Velocity.zero, None);
    val trackedMovable = MovableTrackerProxy.track(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setShape(Circle(Point(6, 6), 0));
    Assert.assertTrue(event.isDefined);
  }
  
  @Test
  def testSettingSamePositionNoEventInterception = {
    var event:Option[MotionEvent[MovableMock]] = None;
    
    val movableMock = new MovableMock(Point(5, 5), Velocity.zero, None);
    val trackedMovable = MovableTrackerProxy.track(movableMock);
    trackedMovable.onMotion(e => event = Some(e));
    
    trackedMovable.setShape(Circle(Point(5, 5), 0));
    Assert.assertTrue(event.isEmpty);
  }
}