package scape.scape2d.engine.time

sealed trait TimeUnit {
  def milliseconds:Double;
}

case object Day extends TimeUnit {
  val milliseconds = Hour.milliseconds * 24;
}

case object Hour extends TimeUnit {
  val milliseconds = Minute.milliseconds * 60;
}

case object Minute extends TimeUnit {
  val milliseconds = Second.milliseconds * 60;
}

case object Second extends TimeUnit {
  val milliseconds = Millisecond.milliseconds * 1000;
}

case object Millisecond extends TimeUnit {
  val milliseconds = 1d;
}

case object Nanosecond extends TimeUnit {
  val milliseconds = Millisecond.milliseconds / 1000000;
}