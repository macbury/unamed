package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Monster;
import com.macbury.unamed.component.TalkingSystem;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.intefrace.MessageInterface;
import com.macbury.unamed.npc.MessagesQueue;
import com.macbury.unamed.npc.PlayerTriggers;

public class Npc extends Character implements PlayerTriggers, MessageInterface {

  private static final int LIGHT_POWER = 7;

  public Npc() throws SlickException {
    super();
    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    addComponent(light);
    
    charactedAnimation.loadCharacterImage("chars/base");
  }
  
  @Override
  public void onActionButton(Player player) throws SlickException {
    TileBasedMovement movement = (TileBasedMovement) getComponent(TileBasedMovement.class);
    movement.lookAt(player);
    
    MessagesQueue dialog = new MessagesQueue();
    dialog.add("Hello!");
    dialog.add("I m simple npc!");
    dialog.add("I just live here underground and wait for my death");
    
    InterfaceManager.shared().showDialogue(dialog, this);
  }

  @Override
  public void onDialogueEnd(MessagesQueue messages) {
    
  }

}
