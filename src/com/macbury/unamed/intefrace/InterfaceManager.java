package com.macbury.unamed.intefrace;

import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.intefrace.developerconsole.DeveloperConsole;
import com.macbury.unamed.npc.MessagesQueue;
import com.macbury.unamed.scenes.MenuScene;

public class InterfaceManager extends Stack<Interface> implements TimerInterface {
  
  private DeveloperConsole developerConsole;
  private final static String CURSOR_TEXT     = "|";
  private String cursorText                   = "";
  
  private final static int CURSOR_BLINK_DELAY = 500;
  private static final short INTERFACE_KEYBOARD_TIMEOUT = 200;
  private int cursorBlinkTime                 = 0;
  private static InterfaceManager shared;
  
  Graphics interfaceGraphics;
  Image    interfaceImage;
  Timer    blockInputTimer;
  private MessageBoxInterface messageBox;
  
  public static InterfaceManager shared() throws SlickException {
    if (shared == null) {
      shared = new InterfaceManager();
    }
    
    return shared;
  }
  
  @Override
  public void clear() {
    super.clear();
    this.messageBox.reset();
  }

  public InterfaceManager() throws SlickException {
    this.developerConsole  = new DeveloperConsole();
    this.messageBox        = new MessageBoxInterface();
    this.blockInputTimer   = new Timer(INTERFACE_KEYBOARD_TIMEOUT, this);
    blockInputTimer.stop();
  }
  
  public boolean isOpened(Interface inte) {
    for (Interface in : this) {
      if (in == inte) {
        return true;
      }
    }
    return false;
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    if (currentInterface() != null && currentInterface().shouldRenderOnlyThis()) {
      currentInterface().render(gc, sb, gr);
    } else {
      for (Interface inte : this) {
        inte.render(gc, sb, gr);
      }
    }
    
  }
  
  public Graphics getInterfaceContexts() throws SlickException {
    if (this.interfaceGraphics == null) {
      GameContainer gc       = Core.instance().getContainer();
      this.interfaceImage    = new Image(gc.getWidth(), gc.getHeight());
      Core.log(this.getClass(),"Initializing interface Context: " + gc.getWidth() + " x " + gc.getHeight());
      this.interfaceGraphics = interfaceImage.getGraphics();
    }
    return this.interfaceGraphics;
  }
  
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    blockInputTimer.update(delta);
    Input input = gc.getInput();
    cursorBlinkTime -= delta;
    
    if (cursorBlinkTime <= 0) {
      cursorBlinkTime = CURSOR_BLINK_DELAY;
      if (cursorText == CURSOR_TEXT) {
        cursorText = " ";
      } else {
        cursorText = CURSOR_TEXT;
      }
    }
    
    
    
   // if (Core.DEBUG) {
      if (input.isKeyPressed(Input.KEY_GRAVE)) {
        if (isOpened(this.developerConsole)) {
          this.close(this.developerConsole);
        } else {
          this.push(developerConsole);
        }
      }
 //   }
    
    Interface inte = currentInterface();
    if (inte != null) {
      inte.update(gc, sb, delta);
    }
  }

  public void close(Interface face) {
    Core.log(this.getClass(),"Closing interface: " + face.getClass().getSimpleName());
    Interface currentlyOpenedInterface = null;
    while(true) {
      currentlyOpenedInterface = this.pop();
      if (currentlyOpenedInterface.equals(face)) {
        break;
      }
    }
    Core.instance().getInput().pause();
    blockInputTimer.restart();
  }

  public Interface currentInterface() {
    try {
      return this.get(this.size() - 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
  
  public boolean shouldBlockGamePlay() {
    return (currentInterface() == null || currentInterface().shouldBlockGamePlay() || blockInputTimer.running());
  }

  public String getCursorText() {
    return cursorText;
  }

  @Override
  public synchronized Interface pop() {
    Interface inte = super.pop(); 
    inte.onExit();
    currentInterface().onEnter();
    return inte;
  }

  @Override
  public Interface push(Interface inte) {
    if (currentInterface() != null) {
      currentInterface().onExit();
    }
    Core.instance().getInput().clearKeyPressedRecord();
    inte.onEnter();
    Core.log(this.getClass(),"Opening inteface:" + inte.getClass().getSimpleName() + " of opened: " + this.size());
    return super.push(inte);
  }
  
  public void showDialogue(MessagesQueue queue, MessageInterface delegate) throws SlickException {
    messageBox.setDialogue(queue, delegate);
    push(messageBox);
  }
  
  public void drawTextWithShadow(int textX, int textY, String text) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    font.drawString(textX+2, textY+2, text, Color.black);
    font.drawString(textX, textY, text);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    blockInputTimer.stop();
    Input input = Core.instance().getInput();
    input.clearControlPressedRecord();
    input.clearKeyPressedRecord();
    input.clearMousePressedRecord();
    input.resume();
  }
}
