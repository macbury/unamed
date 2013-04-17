package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.npc.MessagesQueue;

public class MessageBoxInterface extends Interface implements TimerInterface {
  private static final byte STATE_IDLE     = 0;
  private static final byte STATE_PRINTING = 1;
  private static final byte STATE_WAITING_FOR_INPUT_TO_GO_TO_THE_NEXT_MESSAGE = 2;
  private static final byte STATE_CLOSE_WINDOW = 3;
  private byte currentState = STATE_IDLE;
  private static final short TEXT_SPEED = 50;
  
  private MessagesQueue    messages;
  private MessageInterface delegate;
  private MessageBox messageBox;
  private Timer messageTimer;
  
  private int currentMessageCharIndex;
  private int currentMessageIndex;
  private char[] currentMessage;
  private String currentTextToDisplay = "";
  
  public MessageBoxInterface() {
    this.messageTimer = new Timer(TEXT_SPEED, this);
    this.messageBox   = new MessageBox(0,0,640,180);
  }
  
  public void setDialogue(MessagesQueue messages, MessageInterface delegate) throws SlickException {
    this.messages   = (MessagesQueue) messages.clone();
    this.delegate   = delegate;
    currentMessage  = null;
    currentMessageIndex = -1;
    currentTextToDisplay = "";
    this.messageTimer.setEnabled(true);
    if (this.currentState != STATE_IDLE) {
      throw new SlickException("There is ongoing message!");
    }
    nextMessage();
  }
  
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    messageBox.setCenterX(gc.getWidth()/2);
    messageBox.setY(gc.getHeight() - messageBox.getHeight() - 40);
    messageBox.draw(gr);
    gr.pushTransform();
    gr.translate(messageBox.getX() + 15, messageBox.getY() + 15);
    InterfaceManager.shared().drawTextWithShadow(0, 0, currentTextToDisplay);
    gr.popTransform();
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    messageTimer.update(delta);
    messageTimer.setEnabled(this.currentState == STATE_PRINTING);
    
    Input input = gc.getInput();
    
    if (this.currentState == STATE_WAITING_FOR_INPUT_TO_GO_TO_THE_NEXT_MESSAGE) {
      if(input.isKeyPressed(Core.ACTION_KEY)) {
        nextMessage();
      }
    } else if (this.currentState == STATE_PRINTING) {
      if(input.isKeyPressed(Core.ACTION_KEY)) {
        finishMessage();
      }
    } else if (this.currentState == STATE_CLOSE_WINDOW) {
      InterfaceManager.shared().pop();
      this.currentState = STATE_IDLE;
    }
  }

  private void finishMessage() {
    this.currentMessageCharIndex = currentMessage.length;
    this.currentTextToDisplay    = new String(currentMessage);
  }

  @Override
  public void onEnter() {
    
  }

  @Override
  public void onExit() {
    
  }

  @Override
  public boolean shouldBlockGamePlay() {
    return true;
  }
  
  public void nextMessage() {
    if (this.messages.size() == 0) {
      if (delegate != null) {
        delegate.onDialogueEnd(this.messages);
      }
      this.currentState       = STATE_CLOSE_WINDOW;
    } else {
      currentMessage          = this.messages.remove(0).toCharArray();
      currentTextToDisplay    = "";
      currentMessageCharIndex = 0; 
      this.currentState       = STATE_PRINTING;
    }
  }
  
  @Override
  public void onTimerFire(Timer timer) {
    if (currentMessage != null) {
      if (currentMessageCharIndex >= currentMessage.length) {
        currentMessage    = null;
        this.currentState = STATE_WAITING_FOR_INPUT_TO_GO_TO_THE_NEXT_MESSAGE;
      } else {
        currentTextToDisplay += currentMessage[currentMessageCharIndex];
        currentMessageCharIndex++;
      }
    }
  }

}
