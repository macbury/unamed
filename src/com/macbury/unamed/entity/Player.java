package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

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

}
