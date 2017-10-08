package scape.scape2d.graphics.rgb

object RgbColorModel {
  val additive:(Int, Int) => Int = (rgb1, rgb2) => Math.min(rgb1 + rgb2, 255);
}