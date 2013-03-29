package com.macbury.unamed.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.TorchItem;

public class Player extends Entity {
  final static int MIN_INVENTORY_INDEX = 1;
  final static int MAX_INVENTORY_INDEX = 10;
  public final static int FOG_OF_WAR_RADIUS = 10;
  private static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final int LIGHT_POWER      = 10;
  TileBasedMovement tileMovement;
  KeyboardMovement  keyboardMovement;
  
  final static int MAX_THROTTLE_TIME = 500;
  
  
  int currentInventoryIndex = 0;
  
  int buttonThrottle = 0;
  boolean pressedActionKey = false;
  
  InventoryItem[] hotBarItems;
  
  public Player() throws SlickException {
    super();
    
    this.hotBarItems = new InventoryItem[MAX_INVENTORY_INDEX];
    buildInventory();
    this.z = ENTITY_ZINDEX;
    tileMovement = new TileBasedMovement();
    addComponent(tileMovement);
    
    keyboardMovement = new KeyboardMovement();
    addComponent(keyboardMovement);
    
    CharacterAnimation characterAnimationComponent = new CharacterAnimation();
    addComponent(characterAnimationComponent);
    characterAnimationComponent.loadCharacterImage("base");

    solid = true;
    if (Core.DEBUG) {
      addComponent(new HitBox());
    }
    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    addComponent(light);
  }
  
  private void buildInventory() {
    this.hotBarItems[0] = new TorchItem(this);
  }
  /*
   * Return position as tiles cordinates
   */
  public Vector2f getTilePositionInFront() {
    int dx = this.getTileX();
    int dy = this.getTileY();
    
    switch (tileMovement.direction) {
      case TileBasedMovement.DIRECTION_DOWN:
        dy += 1;
      break;

      case TileBasedMovement.DIRECTION_TOP:
        dy -= 1;
      break;
      
      case TileBasedMovement.DIRECTION_LEFT:
        dx -= 1;
      break;
      
      case TileBasedMovement.DIRECTION_RIGHT:
        dx += 1;
      break;
    }
    
    if (!this.getLevel().isSolid(dx, dy)) {
      return new Vector2f(dx, dy);
    } else {
      return null;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    Input input    = gc.getInput();
    
    if (pressedActionKey) {
      buttonThrottle += delta;
    }
    
    if (buttonThrottle > MAX_THROTTLE_TIME) {
      pressedActionKey = false;
    }
    if(input.isKeyDown(Input.KEY_Z) && !pressedActionKey) {
      InventoryItem currentItem = getCurrentHotBarItem();
      
      if (currentItem != null) {
        if (!currentItem.use())
          SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
      }
      
      pressedActionKey = true;
      buttonThrottle   = 0;
    }
    
    if (input.isKeyPressed(Input.KEY_0)) {
      setInventoryIndex(10);
    } else if (input.isKeyPressed(Input.KEY_1)) {
      setInventoryIndex(1);
    } else if (input.isKeyPressed(Input.KEY_2)) {
      setInventoryIndex(2);
    } else if (input.isKeyPressed(Input.KEY_3)) {
      setInventoryIndex(3);
    } else if (input.isKeyPressed(Input.KEY_4)) {
      setInventoryIndex(4);
    } else if (input.isKeyPressed(Input.KEY_5)) {
      setInventoryIndex(5);
    } else if (input.isKeyPressed(Input.KEY_6)) {
      setInventoryIndex(6);
    } else if (input.isKeyPressed(Input.KEY_7)) {
      setInventoryIndex(7);
    } else if (input.isKeyPressed(Input.KEY_8)) {
      setInventoryIndex(8);
    } else if (input.isKeyPressed(Input.KEY_9)) {
      setInventoryIndex(9);
    }
  }

  
  public InventoryItem getCurrentHotBarItem() {
    return this.hotBarItems[this.currentInventoryIndex];
  }
  
  public void setInventoryIndex(int index) {
    index = Math.min(index, MAX_INVENTORY_INDEX);
    index = Math.max(index, MIN_INVENTORY_INDEX);
    this.currentInventoryIndex = index-1;
  }
}
