package com.macbury.unamed.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.macbury.unamed.Core;

public class EntityFactory {
  
  static public Entity createPlayer() throws SlickException {
    Entity e = new Entity("Player");
    e.addComponent(new TileBasedMovement());
    e.addComponent(new KeyboardMovement());
    CharacterAnimation characterAnimationComponent = new CharacterAnimation();
    e.addComponent(characterAnimationComponent);
    characterAnimationComponent.loadCharacterImage("base");
    e.solid = true;
    if (Core.DEBUG) {
      e.addComponent(new HitBox());
    }
    
    return e;
  }
  
}
