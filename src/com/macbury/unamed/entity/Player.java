package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.TileBasedMovement;

public class Player extends Entity {
  public final static int FOG_OF_WAR_RADIUS = 5;
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
  }

}
