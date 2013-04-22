package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

public class Monster extends Character {

  public Monster() throws SlickException {
    super();
    this.charactedAnimation.loadCharacterImage("chars/monster");
    this.addComponent(new com.macbury.unamed.component.Monster());
  }
}
