package com.macbury.unamed;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.WorldBuilder;
import com.macbury.unamed.scenes.GameScene;
import com.macbury.unamed.scenes.LoadingState;

public class Core extends StateBasedGame {
  public static final boolean DEBUG   = false;
  public static final int MIN_UPDATES = 20;
  public static final int MAX_UPDATES = 10;
  public static final int MAX_FPS     = 60;
  public final static int TILE_SIZE   = 32;
  private static Core coreInstance;
  public static String title = "Unamed";
  
  private UnicodeFont font;
  
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
    this.addState(new LoadingState());
    //this.addState(new PerlinTestState(gameContainer));
    //this.addState(new GameScene(gameContainer));

    
  }

}
