package scape.scape2d.engine.process

import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.IoCDeferred

class ProcessCalculatorMock(
  private var _counter:Int
) extends Process {
  // this default constructor is only exists for cglib proxy support
  def this() = this(0);
  
  def counter = _counter;
  
  @IoCDeferred
  def *(multiplier:Int) = _counter *= multiplier;
  
  def /(divider:Int) = _counter /= divider;
  
  def integrate(timestep:Duration) = _counter += timestep.milliseconds.toInt;
}