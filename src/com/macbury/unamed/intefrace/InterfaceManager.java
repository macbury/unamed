package com.macbury.unamed.intefrace;

import java.util.Stack;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.scenes.MenuScene;

public class InterfaceManager extends Stack<Interface> {
  
  private DeveloperConsole developerConsole;
  private final static String CURSOR_TEXT = "|";
  private String cursorText = "";
  
  private final static int CURSOR_BLINK_DELAY = 500;
  private int cursorBlinkTime = 0;
  
  private static InterfaceManager shared;
  
  public static InterfaceManager shared() {
    if (shared == null) {
      shared = new InterfaceManager();
    }
    
    return shared;
  }
  
  public InterfaceManager() {
    this.developerConsole = new DeveloperConsole();
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    for (Interface inte : this) {
      inte.render(gc, sb, gr);
    }
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
    
    if (input.isKeyPressed(Input.KEY_GRAVE)) {
      if (this.indexOf(developerConsole) == -1) {
        this.push(developerConsole);
      } else {
        this.pop();
      }
    }
    
    Interface inte = this.get(this.size() - 1);
    if (inte != null) {
      inte.update(gc, sb, delta);
    }
  }

  public String getCursorText() {
    return cursorText;
  }

}
