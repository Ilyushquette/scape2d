package scape.scape2d.debugger

import scape.scape2d.debugger.view.TreeNodesView
import scape.scape2d.engine.motion.collision.detection.linear.QuadTreeLinearMotionCollisionDetector

class QuadTreeCollisionDetectorDebugger(val treeNodesView:TreeNodesView) {
  def trackNodes(collisionDetector:QuadTreeLinearMotionCollisionDetector[_]) = {
    collisionDetector.addTreeCreationListener(treeNodesView.renderTree);
  }
}