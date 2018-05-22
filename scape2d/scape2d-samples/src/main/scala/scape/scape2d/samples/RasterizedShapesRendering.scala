package scape.scape2d.samples

import java.awt.Color
import java.awt.Dimension
import javax.swing.JFrame
import scape.scape2d.debugger.view.ShapeDrawer
import scape.scape2d.debugger.view.swing.SwingBuffer
import scape.scape2d.debugger.view.swing.SwingMixingRastersShapeDrawer
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.ShapeUnitConverter
import scape.scape2d.graphics.CustomizedShape
import scape.scape2d.graphics.rasterizer.UnitConvertingRasterizer
import scape.scape2d.graphics.rasterizer.cache.CachingRasterizers
import scape.scape2d.graphics.rasterizer.recursive.MidpointCircleRasterizer
import scape.scape2d.graphics.rasterizer.recursive.NaiveSegmentRasterizer
import scape.scape2d.graphics.rasterizer.recursive.RecursiveRasterizer
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle

object RasterizedShapesRendering {
  def main(args:Array[String]) = {
    val converter = ShapeUnitConverter(50);
    val rasterizer = RecursiveRasterizer(
        segmentRasterizer = CachingRasterizers.enhanceSegmentRasterizer(NaiveSegmentRasterizer()),
        circleRasterizer = CachingRasterizers.enhanceCircleRasterizer(MidpointCircleRasterizer())
    );
    val buffer = new SwingBuffer(new Dimension(800, 600), true);
    val unitConvertingRecursiveRasterizer = UnitConvertingRasterizer(converter, rasterizer);
    val shapeDrawer = new SwingMixingRastersShapeDrawer(buffer, unitConvertingRecursiveRasterizer);
    shapeDrawer.setOpaque(false);
    
    val frame = new JFrame("Scape2D Raster rendering");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane.setBackground(Color.BLACK);
    frame.add(shapeDrawer);
    frame.pack();
    frame.setVisible(true);
    
    drawPoint(shapeDrawer);
    drawSegments(shapeDrawer);
    drawAxisAlignedRectangle(shapeDrawer);
    drawCircles(shapeDrawer);
    drawPolygon(shapeDrawer);
    drawCircleSweep(shapeDrawer);
    drawRing(shapeDrawer);
  }
  
  private def drawPoint(shapeDrawer:ShapeDrawer) = {
    val customizedPoints = for(off <- 1d to 3d by 0.1) yield CustomizedShape(Point(off, off), 0xFFFFFFFF);
    customizedPoints.foreach(shapeDrawer.draw);
  }
  
  private def drawSegments(shapeDrawer:ShapeDrawer) = {
    val topBatch = for(x <- 4d to 6d by 0.1) yield CustomizedShape(Segment(Point(5, 2), Point(x, 3)), 0xFFFFFFFF);
    val leftBatch = for(y <- 1d to 3d by 0.1) yield CustomizedShape(Segment(Point(5, 2), Point(4, y)), 0xFFFFFFFF);
    val bottomBatch = for(x <- 4d to 6d by 0.1) yield CustomizedShape(Segment(Point(5, 2), Point(x, 1)), 0xFFFFFFFF);
    val rightBatch = for(y <- 1d to 3d by 0.1) yield CustomizedShape(Segment(Point(5, 2), Point(6, y)), 0xFFFFFFFF);
    
    val customizedSegments = topBatch ++ leftBatch ++ bottomBatch ++ rightBatch;
    customizedSegments.foreach(shapeDrawer.draw);
  }
  
  private def drawAxisAlignedRectangle(shapeDrawer:ShapeDrawer) = {
    shapeDrawer.draw(CustomizedShape(AxisAlignedRectangle(Point(7, 1), 4, 2), 0xFFFFFFFF));
  }
  
  private def drawCircles(shapeDrawer:ShapeDrawer) = {
    val customizedCircles = for(radius <- 0.05 to 1 by 0.1)
                            yield CustomizedShape(Circle(Point(14, 2), radius), 0xFFFFFFFF);
    customizedCircles.foreach(shapeDrawer.draw);
  }
  
  private def drawPolygon(shapeDrawer:ShapeDrawer) = {
    val polygon = PolygonBuilder(Point(1, 4), Point(3, 4), Point(3, 6)).to(Point(2, 5.5)).build;
    shapeDrawer.draw(CustomizedShape(polygon, 0xFFFFFFFF));
  }
  
  private def drawCircleSweep(shapeDrawer:ShapeDrawer) = {
    val circleSweep = CircleSweep(Circle(Point(5, 4.5), 0.5), Vector(3, 20(Degree)));
    shapeDrawer.draw(CustomizedShape(circleSweep, 0xFFFFFFFF));
  }
  
  private def drawRing(shapeDrawer:ShapeDrawer) = {
    val ring = Ring(Circle(Point(11, 5), 1), 0.5);
    shapeDrawer.draw(CustomizedShape(ring, 0xFFFFFFFF));
  }
}