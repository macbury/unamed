package com.macbury.unamed.component;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.Path;

public interface TileBasedMovementCallback {
  void onFinishMovement() throws SlickException;
}
