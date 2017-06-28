package scape.scape2d.debugger.view.swing

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.{Shape => SwingShape}
import java.awt.image.BufferedImage
import java.util.concurrent.ConcurrentLinkedQueue
import org.apache.log4j.Logger
import javax.swing.JPanel
import scape.scape2d.debugger.swing._
import scape.scape2d.debugger.view.ShapeDrawer
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.debugger.view.CustomizedShape

class SwingShapeDrawer(
  val dimension:Dimension,
  val backgroundColor:Color,
  val unitsPerPixel:Double)
extends JPanel with ShapeDrawer {
  private val log = Logger.getLogger(getClass);
  private val buffer = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
  private val renderingQueue = new ConcurrentLinkedQueue[Graphics2D => Unit];
  private val pixelate = (d:Double) => d / unitsPerPixel;
  
  override def getPreferredSize = new Dimension(dimension);
  
  def draw(customizedShape:CustomizedShape) = {
    val mappedShape = map(customizedShape.shape, pixelate);
    val color = new Color(customizedShape.rgbHexcode);
    this ! (render(mappedShape, color, customizedShape.fill, _:Graphics2D));
  }
  
  def clearAndDraw(clearable:Shape, drawableCustomizedShape:CustomizedShape) = {
    val mappedClearable = map(clearable, pixelate);
    val mappedDrawable = map(drawableCustomizedShape.shape, pixelate);
    val color = new Color(drawableCustomizedShape.rgbHexcode);
    this ! (g => {
      render(mappedClearable, backgroundColor, true, g);
      render(mappedDrawable, color, drawableCustomizedShape.fill, g);
    });
  }
  
  def clear(shape:Shape) = {
    val mappedShape = map(shape, pixelate);
    this ! (render(mappedShape, backgroundColor, true, _:Graphics2D));
  }
  
  private def !(update:Graphics2D => Unit) = {
    renderingQueue.offer(update);
    repaint();
  }
  
  private def render(swingShape:SwingShape, color:Color, fill:Boolean, g:Graphics2D) = {
    g.setColor(color);
    if(fill) g.fill(swingShape) else g.draw(swingShape);
  }
  
  override def paint(g:Graphics) = {
    log.debug("Rendering graphics phase starts...");
    var updates = 0;
    while(!renderingQueue.isEmpty()) {
      val gBuffer = buffer.createGraphics();
      gBuffer.translate(0, dimension.height - 30);
      gBuffer.scale(1, -1);
      renderingQueue.poll()(gBuffer);
      gBuffer.dispose();
      updates += 1;
    }
    g.drawImage(buffer, 0, 0, null);
    log.debug("Rendering graphics phase ended. %d updates performed".format(updates));
  }
}