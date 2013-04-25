package com.macbury.unamed.component;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.Path;

public interface TileFollowCallback {
  void onPathComplete(Path pathToFollow)  throws SlickException;
  void onPathError(Path pathToFollow) throws SlickException;
}
