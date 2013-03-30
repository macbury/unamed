package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.component.Light;

public class Torch extends Entity {
  final static int TORCH_POWER       = 10;
  
  public Torch() throws SlickException {
    super();
    
    Light light = new Light();
    light.setLightPower(TORCH_POWER);
    light.updateLight();
    addComponent(light);
    
    AnimatedSprite animatedSprite = new AnimatedSprite("torch");
    addComponent(animatedSprite);
  }


  

}
