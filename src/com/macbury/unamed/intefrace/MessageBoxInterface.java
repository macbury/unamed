package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.npc.MessagesQueue;

public class MessageBoxInterface extends Interface {
  private MessagesQueue    messages;
  private MessageInterface delegate;
  private MessageBox messageBox;

  public void setDialogue(MessagesQueue messages, MessageInterface delegate) {
    this.messages   = (MessagesQueue) messages.clone();
    this.delegate   = delegate;
    this.messageBox = new MessageBox(0,0,640,180);
  }
  
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    messageBox.setCenterX(gc.getWidth()/2);
    messageBox.setY(gc.getHeight() - messageBox.getHeight() - 80);
    messageBox.draw(gr);
    gr.pushTransform();
    gr.translate(messageBox.getX() + 15, messageBox.getY() + 15);
    InterfaceManager.shared().drawTextWithShadow(0, 0, "Hello world!");
    gr.popTransform();
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
  }

  @Override
  public void onEnter() {
    
  }

  @Override
  public void onExit() {
    
  }

  @Override
  public boolean blockEntitiesUpdate() {
    return true;
  }

}
