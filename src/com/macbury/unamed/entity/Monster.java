package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.ai.HostileWanderAI;
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TilePathFollowComponent;

public class Monster extends Character {
  private static final float MONSTER_DEFAULT_SPEED = 0.0020f;
  public RandomMovement randomMovement;
  
  public Monster() throws SlickException {
    super();
    this.charactedAnimation.loadCharacterImage("chars/monster");
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    this.addComponent(new TilePathFollowComponent());
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;

    this.setAi(new HostileWanderAI());
  }

}
