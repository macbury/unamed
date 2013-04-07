package com.macbury.unamed.intefrace;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.CoalItem;
import com.macbury.unamed.inventory.CopperItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.inventory.TorchItem;
import com.macbury.unamed.level.BlockResources;
import com.macbury.unamed.level.Level;

public class InGameInterface {
  final static int HOTBAR_PADDING = 10;
  public final static int HOTBAR_CELL_SIZE = 34;
  public final static int HOTBAR_CELL_TEXT_PADDING = -2;
  Level level;
  private UnicodeFont font;
  private Image cellImage;
  private Image selectedCellImage;
  private SpriteSheet hotBarSpriteSheet;
  private HashMap<String, Image> hotBarIconsCache;
  
  public InGameInterface(Level level) throws SlickException {
    this.level = level;
    this.font  = Core.instance().getFont();
    
    SpriteSheet spriteSheet = ImagesManager.shared().getHotBarCellSpriteSheet();
    
    this.cellImage         = spriteSheet.getSprite(0, 0);
    this.selectedCellImage = spriteSheet.getSprite(1, 0);
    
    this.hotBarSpriteSheet = spriteSheet;
    this.hotBarIconsCache  = new HashMap<>();
  }
  
  public Image getOrLoadInventoryItemImage(Class klass, int x, int y) {
    Image img = hotBarIconsCache.get(klass.getName());
    if (img == null) {
      img = hotBarSpriteSheet.getSubImage(x, y);
      hotBarIconsCache.put(klass.getName(), img);
    }
    return img;
  }
  
  public Image getImageForInventoryItem(InventoryItem item) throws SlickException {
    if (CopperItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CopperItem.class, 1, 1);
    } else if (CoalItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CoalItem.class, 1, 1);
    } if (TorchItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(TorchItem.class, 2, 0);
    }
    
    throw new SlickException("No image for inventory item: " +item.getClass().getName());    
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    Player player = level.getPlayer();
    gr.setColor(Color.white);
    
    font.drawString(10, 10, "FPS: "+ gc.getFPS());
    
    int cellCount = InventoryManager.MAX_HOTBAR_INVENTORY_INDEX - InventoryManager.MIN_HOTBAR_INVENTORY_INDEX;

    gr.pushTransform();
    gr.translate(HOTBAR_PADDING, gc.getHeight() - HOTBAR_CELL_SIZE - HOTBAR_PADDING); // MOVE origin to left bottom corner of the screem 
    
    int x = 0;
    for(int i = 0; i <= cellCount; i++) {
      InventoryItem item = player.inventory.getItem(i);
      if (player.inventory.getCurrentHotBarIndex() == i) {
        this.selectedCellImage.draw(x, 0);
      } else {
        this.cellImage.draw(x, 0);
      }
      
      if (item != null) {
        String countText    = Integer.toString(item.getCount());
        int countTextWidth  = font.getWidth(countText);
        int countTextHeight = font.getHeight(countText);
        
        if (BlockItem.class.isInstance(item)) {
          BlockItem blockItem = (BlockItem) item;
          Image icon = BlockResources.shared().imageForBlockClass(blockItem.blockType);
          icon.draw(x+4, 4, 0.8f);
        } else {
          Image icon = getImageForInventoryItem(item);
          icon.draw(x, 0);
        }
        
        int textX = x + HOTBAR_CELL_SIZE - countTextWidth - HOTBAR_CELL_TEXT_PADDING;
        int textY = HOTBAR_CELL_SIZE - countTextHeight - HOTBAR_CELL_TEXT_PADDING;
        
        font.drawString(textX, textY, countText);
        drawTextWithShadow(textX, textY, countText);
      }
      
      x += HOTBAR_CELL_SIZE + HOTBAR_PADDING;
    }
    
    gr.popTransform();
    //hotBarImage.drawCentered(gc.getWidth()/2, gc.getHeight() - hotBarImage.getHeight() - 10);
  }

  private void drawTextWithShadow(int textX, int textY, String text) {
    font.drawString(textX+2, textY+2, text, Color.black);
    font.drawString(textX, textY, text);
  }

}
