package scape.scape2d.debugger.view.swing

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.util.concurrent.ConcurrentLinkedQueue

import org.apache.log4j.Logger

import javax.swing.JPanel
import scape.scape2d.debugger.view.Circle
import scape.scape2d.debugger.view.Rectangle
import scape.scape2d.debugger.view.Shape
import scape.scape2d.debugger.view.ShapeDrawer

class SwingShapeDrawer(val dimension:Dimension, val backgroundColor:Color) extends JPanel with ShapeDrawer {
  private val log = Logger.getLogger(getClass);
  private val buffer = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
  private val renderingQueue = new ConcurrentLinkedQueue[Graphics2D => Unit];
  
  override def getPreferredSize = new Dimension(dimension);
  
  override def draw(shape:Shape) = this ! (render(shape, backgroundColor, _:Graphics2D));
  
  override def clearAndDraw(clearable:Shape, drawable:Shape) = {
    this ! (g => {
      render(clearable, Color.BLACK, g);
      render(drawable, Color.RED, g);
    });
  }
  
  override def clear(shape:Shape) = this ! (render(shape, backgroundColor, _:Graphics2D));
  
  private def !(update:Graphics2D => Unit) = {
    renderingQueue.offer(update);
    repaint();
  }
  
  private def render(shape:Shape, color:Color, g:Graphics2D) = {
    val swingShape = shape match {
      case Circle(x, y, radius) => new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
      case Rectangle(x1, y1, width, height) => new Rectangle2D.Double(x1, y1, width, height);
      case _ => throw new IllegalArgumentException("Couldn't match %s to Swing shape".format(shape));
    }
    
    g.setColor(color);
    g.fill(swingShape);
  }
  
  override def paint(g:Graphics) = {
    log.debug("Rendering graphics phase starts...");
    var updates = 0;
    while(!renderingQueue.isEmpty()) {
      val gBuffer = buffer.createGraphics();
      renderingQueue.poll()(gBuffer);
      gBuffer.dispose();
      updates += 1;
    }
    g.drawImage(buffer, 0, 0, null);
    log.debug("Rendering graphics phase ended. %d updates performed".format(updates));
  }
}