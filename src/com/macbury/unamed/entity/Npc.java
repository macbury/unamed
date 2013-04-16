package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Monster;
import com.macbury.unamed.component.TalkingSystem;
import com.macbury.unamed.npc.MessagesQueue;

public class Npc extends Character {

  private static final int LIGHT_POWER = 5;
  private TalkingSystem talkingSystem;

  public Npc() throws SlickException {
    super();
    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    addComponent(light);
    
    charactedAnimation.loadCharacterImage("chars/base");
    
    MessagesQueue dialog = new MessagesQueue();
    dialog.add("Hello!");
    dialog.add("I m simple npc!");
    dialog.add("I just live here underground and wait for my death");
    this.talkingSystem = new TalkingSystem(dialog);
    addComponent(this.talkingSystem);
    
    addComponent(new Monster());
  }

}
