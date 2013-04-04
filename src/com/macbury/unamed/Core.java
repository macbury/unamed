package com.macbury.unamed;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.WorldBuilder;

public class Core extends StateBasedGame {
  public static final boolean DEBUG   = false;
  public static final int MIN_UPDATES = 20;
  public static final int MAX_UPDATES = 10;
  public static final int MAX_FPS     = 60;
  public final static int TILE_SIZE   = 32;
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
    //this.addState(new PerlinTestState(gameContainer));
    //this.addState(new GameScene(gameContainer));
    
    WorldBuilder world = new WorldBuilder(1024, (int) Math.round(1000));
    world.dumpTo("screenshot.png");
    System.exit(0);
  }

}
