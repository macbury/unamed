package com.macbury.unamed.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EntityFactory {
  
  static public Entity createPlayer() throws SlickException {
    Entity e = new Entity("Player");
    e.addComponent(new ImageRenderComponent("ImageRender", new Image("res/images/player.png")));
    e.addComponent(new TileBasedMovement(TileBasedMovement.NAME));
    e.addComponent(new KeyboardMovement("Movement"));
    return e;
  }
  
}
