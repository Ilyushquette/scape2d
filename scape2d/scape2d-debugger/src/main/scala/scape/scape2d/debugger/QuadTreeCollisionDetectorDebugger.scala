package scape.scape2d.debugger

import scape.scape2d.debugger.view.QuadTreeNodesView
import scape.scape2d.engine.motion.collision.detection.QuadTreeBasedCollisionDetector

class QuadTreeCollisionDetectorDebugger(val quadTreeNodesView:QuadTreeNodesView) {
  def trackNodes(collisionDetector:QuadTreeBasedCollisionDetector[_]) = {
    collisionDetector.addTreeCreationListener(quadTreeNodesView.renderTree);
  }
}