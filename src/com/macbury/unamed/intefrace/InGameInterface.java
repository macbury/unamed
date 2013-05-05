package com.macbury.unamed.intefrace;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.FpsGraph;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.block.BlockResources;
import com.macbury.unamed.component.HealthComponent;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.CoalItem;
import com.macbury.unamed.inventory.CopperItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.inventory.TorchItem;
import com.macbury.unamed.level.Level;

public class InGameInterface extends Interface{
  final static int HOTBAR_PADDING = 10;
  public final static int HOTBAR_CELL_SIZE = 34;
  public final static int HOTBAR_CELL_TEXT_PADDING = -2;
  private static final int HEALTH_BAR_BOTTOM = 66;
  private UnicodeFont font;
  private Image cellImage;
  private Image selectedCellImage;
  private SpriteSheet hotBarSpriteSheet;
  private HashMap<String, Image> hotBarIconsCache;
  private FpsGraph fpsMap;
  
  public InGameInterface() throws SlickException {
    this.font  = Core.instance().getFont();
    SpriteSheet spriteSheet = ImagesManager.shared().getHotBarCellSpriteSheet();
    
    this.cellImage         = spriteSheet.getSprite(0, 0);
    this.selectedCellImage = spriteSheet.getSprite(1, 0);
    
    this.hotBarSpriteSheet = spriteSheet;
    if (Core.DEBUG) {
      this.fpsMap            = new FpsGraph();
    }
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    Player player          = Level.shared().getPlayer();
    HealthComponent health = player.getHealth();
    
    
    //if (Core.DEBUG) {
    //  font.drawString(10, gc.getHeight() - 30, "FPS: "+ gc.getFPS());
    //}
    
    if (this.fpsMap != null) {
      this.fpsMap.render(gr);
    }
    
    gr.setColor(Color.white);
    int cellCount = InventoryManager.MAX_HOTBAR_INVENTORY_INDEX - InventoryManager.MIN_HOTBAR_INVENTORY_INDEX;
    
    gr.pushTransform();
    gr.translate(HOTBAR_PADDING, HOTBAR_PADDING * 2 + HOTBAR_CELL_SIZE );
    
    gr.setColor(Color.red);
    gr.fillRect(0, 0, 320 * health.getHealthProgress(), 14);
    gr.setColor(Color.white);
    gr.drawRect(0, 0, 320, 14);
    
    gr.popTransform();
    
    gr.pushTransform();
    gr.translate(HOTBAR_PADDING, HOTBAR_PADDING); // MOVE origin to left bottom corner of the screem 
    
    int x = 0;
    for(int i = 0; i <= cellCount; i++) {
      InventoryItem item = InventoryManager.shared().getItem(i);
      if (InventoryManager.shared().getCurrentHotBarIndex() == i) {
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
          Image icon = InventoryManager.shared().getImageForInventoryItem(item);
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

  private void drawTextWithShadow(int textX, int textY, String text) throws SlickException {
    InterfaceManager.shared().drawTextWithShadow(textX, textY, text);
  }


  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    Input input = gc.getInput();
    if (input.isKeyPressed(Input.KEY_ESCAPE)) {
      InterfaceManager.shared().push(new GameMenuInterface());
    }
    
    if (this.fpsMap != null) {
      this.fpsMap.update(delta);
    }
  }

  @Override
  public void onEnter() {
    Player player = Level.shared().getPlayer();
    player.setKeyboardEnabled(true);
  }

  @Override
  public void onExit() {
    Player player = Level.shared().getPlayer();
    player.setKeyboardEnabled(false);
  }

  @Override
  public boolean shouldBlockGamePlay() {
    return false;
  }

  @Override
  public boolean shouldRenderOnlyThis() {
    return false;
  }

}
