package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.Light;
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
    dialog.add("This is multiline testing of long text inputed inside eclipse, it should word wrap and do other crazy stuff like not going from borders and other funny shit like doing other stuff and aother doing it bla bla bla bla do here something, dragons here, hero bla bla long time ago");
    dialog.add("Hello!");
    dialog.add("I m simple npc!");
    dialog.add("I just live here underground and wait for my death");
    dialog.add("Now i im testing my vocals and other shit");
    dialog.add("Testing new line\nline1\nline2\nline3\nline4\nline5\nline6\nline7");
    dialog.add("Testing tabs \t\t\t TEST");
    dialog.add("Testing very long text");
    
    InterfaceManager.shared().showDialogue(dialog, this);
  }

  @Override
  public void onDialogueEnd(MessagesQueue messages) {
    
  }

}
