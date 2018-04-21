package scape.scape2d.graphics.rgb

import java.lang.Math.min

object RgbColorModel {
  val additive:(Int, Int) => Int = (rgb1, rgb2) => min(rgb1 + rgb2, 255);
}