package com.macbury.unamed;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.scenes.GameScene;

public class Core extends StateBasedGame {

  public static final int MIN_UPDATES = 20;
  public static final int MAX_UPDATES = 10;
  public static final int MAX_FPS     = 60;
  private static Core coreInstance;
  public static String title = "Unamed";
  static public Core instance() {
    return Core.coreInstance;
  }

  public Core(String title) {
    super(title);
    Core.coreInstance = this;
  }


  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    this.addState(new GameScene(gameContainer));
  }

  
}
