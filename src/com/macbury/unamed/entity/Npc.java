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
    dialog.add("Now i im testing my vocals and other shit");
    dialog.add("Testing new line\nline1\nline2\nline3\nline4\nline5\nline6\nline7");
    dialog.add("Testing tabs \t\t\t TEST");
    dialog.add("Testing very long text");
    dialog.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed hendrerit dolor. Duis eget congue velit. Donec neque leo, viverra eget condimentum ultricies, cursus id dui. Nulla facilisi. Nam ut quam eu dolor consectetur tempus. Quisque gravida pulvinar arcu, non vehicula dolor congue quis. Morbi at odio non dolor bibendum sodales eget eget tellus. Ut rhoncus risus sit amet arcu aliquam tincidunt. Suspendisse at nibh sapien. Fusce elit libero, tempor quis suscipit sit amet, hendrerit et arcu. Donec quam justo, tincidunt quis vulputate quis, pharetra ut augue. Proin at nulla ut nulla dictum eleifend. Aenean facilisis ornare urna, sit amet ullamcorper risus suscipit eu. Morbi mi sapien, tincidunt vulputate dictum ac, mollis non mauris.");
    InterfaceManager.shared().showDialogue(dialog, this);
  }

  @Override
  public void onDialogueEnd(MessagesQueue messages) {
    
  }

}
