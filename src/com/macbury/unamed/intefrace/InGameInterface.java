package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.level.Level;

public class InGameInterface {
  Level level;
  private UnicodeFont font;
  private Image cellImage;
  private Image selectedCellImage;
  
  public InGameInterface(Level level) throws SlickException {
    this.level = level;
    this.font  = new UnicodeFont("/res/fonts/advocut-webfont.ttf", 20, false, false);
    
    font.addAsciiGlyphs();
    font.getEffects().add(new ColorEffect());
    font.loadGlyphs();
    
    SpriteSheet spriteSheet = new SpriteSheet("res/images/hud/hotbar_cell.png", 34, 34);
    
    this.cellImage         = spriteSheet.getSprite(0, 0);
    this.selectedCellImage = spriteSheet.getSprite(1, 0);
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
    
    int cellCount = Player.MAX_INVENTORY_INDEX - Player.MIN_INVENTORY_INDEX;

    gr.pushTransform();
    gr.translate(10, gc.getHeight() - 52);
    
    for(int x = 0; x <= cellCount; x++) {
      if (player.currentInventoryIndex == x) {
        this.selectedCellImage.draw((x * 34), 10);
      } else {
        this.cellImage.draw((x * 34), 10);
      }
      
    }
    
    gr.popTransform();
    //hotBarImage.drawCentered(gc.getWidth()/2, gc.getHeight() - hotBarImage.getHeight() - 10);
  }

}
