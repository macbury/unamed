package com.macbury.unamed;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryo.Kryo;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.CoalOre;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.Sand;
import com.macbury.unamed.scenes.GameplayScene;
import com.macbury.unamed.scenes.GeneratingWorldState;
import com.macbury.unamed.scenes.LoadingResourceScreen;
import com.macbury.unamed.scenes.MenuScene;
import com.macbury.unamed.serializers.BlockSerializer;
import com.macbury.unamed.serializers.EntitySerializer;
import com.macbury.unamed.serializers.LevelSerializer;

public class Core extends StateBasedGame {
  public static boolean DEBUG         = true;
  public static final int MIN_UPDATES = 20;
  public static final int MAX_UPDATES = 10;
  public static final int MAX_FPS     = 60;
  public final static int TILE_SIZE   = 32;
  public final static int SHADOW_SIZE = 22;
  public static final int ACTION_KEY  = Input.KEY_Z;
  public static final int CANCEL_KEY  = Input.KEY_X;
  private static Core coreInstance;
  public static String title = "Unamed";
  public static final String DUNGON_FILE_NAME = "my.dungeon";
  public static final int WINDOW_WIDTH  = 1280;
  public static final int WINDOW_HEIGHT = 720;
  private UnicodeFont font;
  private GeneratingWorldState generatingWorldState;
  private GameplayScene gameplayScene;
  
  static public Core instance() {
    return Core.coreInstance;
  }

  public UnicodeFont getFont() throws SlickException {
    if (font == null) {
      font = new UnicodeFont("/res/fonts/advocut-webfont.ttf", 20, false, false);
      
      font.addAsciiGlyphs();
      font.getEffects().add(new ColorEffect());
      font.loadGlyphs();
    }
    
    return font;
  }
  
  public Core(String title) {
    super(title);
    Core.coreInstance = this;
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    this.addState(new LoadingResourceScreen());
    this.addState(new MenuScene());
    this.addState(getGeneratingWorldState());
    this.addState(getGameplayScene());
  }
  
  public void resetGame() {
    gameplayScene = null;
  }
  
  private GameState getGameplayScene() {
    if (gameplayScene == null) {
      gameplayScene = new GameplayScene();
    }
    return gameplayScene;
  }

  public GeneratingWorldState getGeneratingWorldState() {
    if (generatingWorldState == null) {
      generatingWorldState = new GeneratingWorldState();
    }
    
    return generatingWorldState;
  }
  
  public File getSaveDirectory(String filename) {
    String homeDirectory = System.getProperty("user.home");
    homeDirectory += File.separator + "Saved Games" + File.separator + "Unamed" + File.separator;
    
    File dir = new File(homeDirectory);
    
    if (!dir.exists()) {
      dir.mkdir();
    }
    
    homeDirectory += "my.dungeon";
    return new File(homeDirectory);
  }

  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
    InputManager.shared().update(delta);
    super.update(gc, delta);
  }
}
