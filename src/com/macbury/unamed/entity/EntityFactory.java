package com.macbury.unamed.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EntityFactory {
  
  static public Entity createTest() throws SlickException {
    Entity e = new Entity("Test");
    e.addComponent(new ImageRenderComponent("ImageRender", new Image("res/images/Night.png")));
    e.addComponent(new TopDownMovement("Movement"));
    return e;
  }
  
}
