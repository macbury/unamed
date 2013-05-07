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
  private UnicodeFont font;
  private FpsGraph fpsMap;
  
  public InGameInterface() throws SlickException {
    this.font  = Core.instance().getFont();

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

    //hotBarImage.drawCentered(gc.getWidth()/2, gc.getHeight() - hotBarImage.getHeight() - 10);
  }



  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    Input input = gc.getInput();
    if (input.isKeyPressed(Input.KEY_ESCAPE)) {
      InterfaceManager.shared().push(new GameMenuInterface());
    }
    
    if (input.isKeyPressed(InventoryInterface.TOGGLE_KEY)) {
      InterfaceManager.shared().push(new InventoryInterface());
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
