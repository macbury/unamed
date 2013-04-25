package com.macbury.unamed;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.Path;

public interface PathFindingCallback {
  void onPathFound(Path path) throws SlickException;
}
