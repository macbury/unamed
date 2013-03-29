package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.level.Level;

public class InGameInterface {
  Level level;
  private UnicodeFont font;
  private Image hotBarImage;
  
  public InGameInterface(Level level) throws SlickException {
    this.level = level;
    this.font  = new UnicodeFont("/res/fonts/advocut-webfont.ttf", 20, false, false);
    
    font.addAsciiGlyphs();
    font.getEffects().add(new ColorEffect());
    font.loadGlyphs();
    
    this.hotBarImage = new Image("res/images/hud/hotbar.png");
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    Player player = level.getPlayer();
    gr.setColor(Color.white);
    InventoryItem currentItem = player.getCurrentHotBarItem();
    
    if (currentItem != null) {
      font.drawString(10, 10, "Selected element: " +currentItem.getName());
    } else {
      font.drawString(10, 10, "Selected element: None");
    }
    
    font.drawString(10, 35, "FPS: "+ gc.getFPS());
    hotBarImage.drawCentered(gc.getWidth()/2, gc.getHeight() - hotBarImage.getHeight() - 10);
  }

}
