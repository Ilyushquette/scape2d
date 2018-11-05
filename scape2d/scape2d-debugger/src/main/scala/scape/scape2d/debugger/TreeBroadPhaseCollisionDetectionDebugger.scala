package scape.scape2d.debugger

import scape.scape2d.debugger.view.TreeNodesView
import scape.scape2d.engine.motion.collision.detection.broad.TreeContinuousBroadPhaseCollisionDetectionStrategy
import scape.scape2d.engine.motion.collision.detection.broad.TreeDiscreteBroadPhaseCollisionDetectionStrategy

case class TreeBroadPhaseCollisionDetectionDebugger(treeNodesView:TreeNodesView) {
  def trackNodes(continuousBroadPhaseCollisionDetectionStrategy:TreeContinuousBroadPhaseCollisionDetectionStrategy[_]) = {
    continuousBroadPhaseCollisionDetectionStrategy onTreeCreation treeNodesView.renderTree;
  }
  
  def trackNodes(discreteBroadPhaseCollisionDetectionStrategy:TreeDiscreteBroadPhaseCollisionDetectionStrategy[_]) = {
    discreteBroadPhaseCollisionDetectionStrategy onTreeCreation treeNodesView.renderTree;
  }
}