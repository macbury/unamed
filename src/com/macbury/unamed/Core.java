package com.macbury.unamed;

import java.io.File;

import org.apache.log4j.Logger;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.macbury.unamed.block.Block;
import com.macbury.unamed.block.CoalOre;
import com.macbury.unamed.block.Dirt;
import com.macbury.unamed.block.Sand;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Monster;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.intefrace.developerconsole.DeveloperConsole;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.scenes.GameplayScene;
import com.macbury.unamed.scenes.GeneratingWorldState;
import com.macbury.unamed.scenes.LoadingResourceScreen;
import com.macbury.unamed.scenes.MenuScene;
import com.macbury.unamed.serializers.BlockSerializer;
import com.macbury.unamed.serializers.EntitySerializer;
import com.macbury.unamed.serializers.LevelSerializer;
import com.macbury.unamed.util.Log4jLogSystem;
import com.macbury.unamed.util.MonsterManager;

public class Core extends StateBasedGame {
  public static final boolean DEBUG_RAYCAST = false;
  public static boolean SHOW_COLLIDERS = false;
  public static boolean DEBUG          = true;
  public static final int MIN_UPDATES = 20;
  public static final int MAX_UPDATES = 10;
  public static final int MAX_FPS     = 60;
  public final static int TILE_SIZE   = 32;
  public final static int SHADOW_SIZE = 22;
  public static final int ACTION_KEY  = Input.KEY_X;
  public static final int CANCEL_KEY  = Input.KEY_Z;
  private static Core coreInstance;
  public static String title            = "Unamed";
  public static final String DUNGON_FILE_NAME = "my.dungeon";
  public static final int WINDOW_WIDTH  = 1280;
  public static final int WINDOW_HEIGHT = 720;
  public static final int FONT_SIZE = 18;
  public static final int FONT_SIZE_CENTER_Y = FONT_SIZE / 2;
  
  private UnicodeFont font;
  private GeneratingWorldState generatingWorldState;
  private GameplayScene gameplayScene;
  private BigImage mainScreenImage;
  
  static public Core instance() {
    return Core.coreInstance;
  }
  
  public BigImage getMainScreenImage() throws SlickException {
    if (this.mainScreenImage == null) {
      this.mainScreenImage = new BigImage("res/images/hud/main_screen.jpg");;
    }
    
    return this.mainScreenImage;
  }
  
  public UnicodeFont getFont() throws SlickException {
    if (font == null) {
      font = new UnicodeFont("/res/fonts/VL-Gothic-Regular.ttf", FONT_SIZE, false, false);
      
      font.addAsciiGlyphs();
      font.getEffects().add(new ColorEffect());
      font.loadGlyphs();
    }
    
    return font;
  }
  
  public Core(String title) {
    super(title);
    Core.coreInstance = this;
    
    Log.setLogSystem(new Log4jLogSystem());
  }
  
  public static void log(Class context, String msg, Throwable error) {
    Logger.getLogger(context).info(msg, error);
    DeveloperConsole.shared().print(msg);
  }
  
  public static void log(Class context, String msg) {
    Logger.getLogger(context).info(msg);
    DeveloperConsole.shared().print(msg);
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    this.addState(new LoadingResourceScreen());
    this.addState(new MenuScene());
    this.addState(getGeneratingWorldState());
    this.addState(getGameplayScene());
  }
  
  public void resetGame() throws SlickException {
    if (gameplayScene != null) {
      SoundManager.shared().stop();
      InterfaceManager.shared().clear();
      MonsterManager.shared().clear();
      InterfaceManager.shared().clear();
      if (Level.shared() != null) {
        Level.shared().clear();
        InventoryManager.shared().clear();
      }
      System.gc();
    }
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

  public void exit() {
    this.getContainer().exit();
  }

  public Input getInput() {
    return this.getContainer().getInput();
  }
}
