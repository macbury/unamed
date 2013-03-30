package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
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
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.inventory.TorchItem;

public class Player extends Entity {
  public final static int FOG_OF_WAR_RADIUS = 10;
  private static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final int LIGHT_POWER      = 10;
  TileBasedMovement tileMovement;
  KeyboardMovement  keyboardMovement;
  
  final static int MAX_PLACING_TIME = 250;
  
  int buttonThrottle = 0;
  private boolean pressedPlaceKey   = false;
  private boolean pressedTakeKey    = false;
  
  public InventoryManager inventory;
  
  
  public Player() throws SlickException {
    super();
    
    this.inventory = new InventoryManager();
    TorchItem item = new TorchItem(this);
    item.addItem(9);
    this.inventory.add(item);
    
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
    
    return new Vector2f(dx, dy);
  }
  
  /*
   * Return position as tiles cordinates for empty tile that is not solid
   */
  public Vector2f getNotSolidTilePositionInFront() {
    Vector2f tileInFront = getTilePositionInFront();
    
    if (!this.getLevel().isSolid((int)tileInFront.getX(), (int)tileInFront.getY())) {
      return tileInFront;
    } else {
      return null;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    Input input    = gc.getInput();
    
    if (pressedPlaceKey) {
      buttonThrottle += delta;
    }
    
    if (buttonThrottle > MAX_PLACING_TIME) {
      pressedPlaceKey = false;
    }
    
    /*if (input.isKeyDown(Input.KEY_X) && !pressedHarvestKey) {
      pressedHarvestKey = true;
      buttonThrottle = 0;
    }*/
    
    if(input.isKeyDown(Input.KEY_Z) && !pressedPlaceKey) {
      InventoryItem currentItem = inventory.getCurrentHotBarItem();
      
      if (currentItem != null) {
        if (currentItem.place()) {
          if (!currentItem.haveItems()) {
            inventory.remove(currentItem);
          }
        } else {
          SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
        }
      }
      
      pressedPlaceKey = true;
      buttonThrottle   = 0;
    }
    
    if (input.isKeyPressed(Input.KEY_0)) {
      inventory.setInventoryIndex(10);
    } else if (input.isKeyPressed(Input.KEY_1)) {
      inventory.setInventoryIndex(1);
    } else if (input.isKeyPressed(Input.KEY_2)) {
      inventory.setInventoryIndex(2);
    } else if (input.isKeyPressed(Input.KEY_3)) {
      inventory.setInventoryIndex(3);
    } else if (input.isKeyPressed(Input.KEY_4)) {
      inventory.setInventoryIndex(4);
    } else if (input.isKeyPressed(Input.KEY_5)) {
      inventory.setInventoryIndex(5);
    } else if (input.isKeyPressed(Input.KEY_6)) {
      inventory.setInventoryIndex(6);
    } else if (input.isKeyPressed(Input.KEY_7)) {
      inventory.setInventoryIndex(7);
    } else if (input.isKeyPressed(Input.KEY_8)) {
      inventory.setInventoryIndex(8);
    } else if (input.isKeyPressed(Input.KEY_9)) {
      inventory.setInventoryIndex(9);
    }
  }

}
