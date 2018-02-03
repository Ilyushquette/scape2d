package scape.scape2d.debugger

import scape.scape2d.debugger.view.QuadTreeNodesView
import scape.scape2d.engine.motion.collision.detection.linear.QuadTreeLinearMotionCollisionDetector

class QuadTreeCollisionDetectorDebugger(val quadTreeNodesView:QuadTreeNodesView) {
  def trackNodes(collisionDetector:QuadTreeLinearMotionCollisionDetector[_]) = {
    collisionDetector.addTreeCreationListener(quadTreeNodesView.renderTree);
  }
}