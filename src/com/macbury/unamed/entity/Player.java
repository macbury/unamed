package com.macbury.unamed.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.TileBasedMovement;

public class Player extends Entity {
  public final static int FOG_OF_WAR_RADIUS = 10;
  private static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final int LIGHT_POWER      = 6;
  TileBasedMovement tileMovement;
  KeyboardMovement  keyboardMovement;
  
  final static int MAX_THROTTLE_TIME = 500;
  
  int buttonThrottle = 0;
  boolean pressedZ = false;
  
  public Player() throws SlickException {
    super();
    
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
    
    if (pressedZ) {
      buttonThrottle += delta;
    }
    
    if (buttonThrottle > MAX_THROTTLE_TIME) {
      pressedZ = false;
    }
    
    if(input.isKeyDown(Input.KEY_Z) && !pressedZ) {
      Vector2f tilePos = getTilePositionInFront();
      if (tilePos != null) {
        pressedZ = true;
        buttonThrottle = 0;
        Torch torch = new Torch();
        this.getLevel().addEntity(torch);
        
        torch.setTilePosition((int)tilePos.x, (int)tilePos.y);
      }
      
    }
  }
  
  
  public void drawInterface(Graphics gr) {
    gr.setColor(Color.white);
    gr.drawString("Selected element: Torch", 10, 30);
    
  }
}
