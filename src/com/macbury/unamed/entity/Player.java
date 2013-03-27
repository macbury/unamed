package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.Core;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.TileBasedMovement;

public class Player extends Entity {
  public final static int FOG_OF_WAR_RADIUS          = 10;
  TileBasedMovement tileMovement;
  KeyboardMovement  keyboardMovement;
  public Player(String id) throws SlickException {
    super(id);
    
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
    this.lightPower = FOG_OF_WAR_RADIUS-1;
  }

}
