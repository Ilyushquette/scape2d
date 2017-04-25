package scape.scape2d.debugger.view

abstract class Shape
case class Circle(x:Int, y:Int, radius:Double) extends Shape;
case class Rectangle(x1:Int, y1:Int, width:Int, height:Int) extends Shape;