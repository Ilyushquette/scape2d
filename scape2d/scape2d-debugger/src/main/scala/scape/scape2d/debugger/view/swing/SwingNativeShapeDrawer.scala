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
import java.awt.AlphaComposite
import scape.scape2d.graphics.CustomizedShape

class SwingNativeShapeDrawer(
  val dimension:Dimension,
  val backgroundColor:Color,
  val unitsPerPixel:Double)
extends JPanel with ShapeDrawer {
  private val log = Logger.getLogger(getClass);
  private val buffer = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
  private val renderingQueue = new ConcurrentLinkedQueue[Graphics2D => Unit];
  private val pixelate = (d:Double) => d / unitsPerPixel;
  
  override def getPreferredSize = new Dimension(dimension);
  
  def draw(customizedShape:CustomizedShape[_ <: Shape]) = {
    val mappedShape = map(customizedShape.shape, pixelate);
    val color = new Color(customizedShape.rgb);
    this ! (render(mappedShape, color, _:Graphics2D));
  }
  
  override def clear(shape:Shape) = {
    val mappedShape = map(shape, pixelate);
    this ! (render(mappedShape, backgroundColor, _:Graphics2D));
  }
  
  private def !(update:Graphics2D => Unit) = {
    renderingQueue.offer(update);
    repaint();
  }
  
  private def render(swingShape:SwingShape, color:Color, g:Graphics2D) = {
    g.setColor(color);
    g.draw(swingShape);
  }
  
  override def paint(g:Graphics) = {
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
    log.info("Rendering graphics phase ended. %d updates performed".format(updates));
  }
}