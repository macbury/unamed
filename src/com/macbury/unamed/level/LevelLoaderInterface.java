package com.macbury.unamed.level;

import org.newdawn.slick.SlickException;

public interface LevelLoaderInterface {
  void onLevelLoad(Level level) throws SlickException;
}
