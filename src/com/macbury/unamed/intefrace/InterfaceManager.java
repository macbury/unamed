package com.macbury.unamed.intefrace;

import java.util.Stack;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.intefrace.developerconsole.DeveloperConsole;
import com.macbury.unamed.scenes.MenuScene;

public class InterfaceManager extends Stack<Interface> {
  
  private DeveloperConsole developerConsole;
  private final static String CURSOR_TEXT     = "|";
  private String cursorText                   = "";
  
  private final static int CURSOR_BLINK_DELAY = 500;
  private int cursorBlinkTime                 = 0;
  private static InterfaceManager shared;
  
  Graphics interfaceGraphics;
  Image    interfaceImage;
  
  public static InterfaceManager shared() throws SlickException {
    if (shared == null) {
      shared = new InterfaceManager();
    }
    
    return shared;
  }
  
  public InterfaceManager() throws SlickException {
    this.developerConsole  = new DeveloperConsole();
    
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    if (this.interfaceGraphics != null) {
      gr.drawImage(interfaceImage, 0, 0);
    }
    
    for (Interface inte : this) {
      inte.render(gc, sb, gr);
    }
  }
  
  public Graphics getInterfaceContexts() throws SlickException {
    if (this.interfaceGraphics == null) {
      GameContainer gc       = Core.instance().getContainer();
      this.interfaceImage    = new Image(gc.getWidth(), gc.getHeight());
      Log.info("Initializing interface Context: " + gc.getWidth() + " x " + gc.getHeight());
      this.interfaceGraphics = interfaceImage.getGraphics();
    }
    return this.interfaceGraphics;
  }
  
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
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
    
    if (input.isKeyPressed(Input.KEY_ESCAPE)) {
      Core.instance().enterState(MenuScene.STATE_MENU);
    }
    
    if (Core.DEBUG) {
      if (input.isKeyPressed(Input.KEY_GRAVE)) {
        if (this.indexOf(developerConsole) == -1) {
          this.push(developerConsole);
        } else {
          this.pop();
        }
      }
      
      Interface inte = currentInterface();
      if (inte != null) {
        inte.update(gc, sb, delta);
      }
    }
  }

  private Interface currentInterface() {
    try {
      return this.get(this.size() - 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
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
    
    inte.onEnter();
    return super.push(inte);
  }
  
  
}