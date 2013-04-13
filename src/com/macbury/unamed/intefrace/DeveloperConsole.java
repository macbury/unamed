package com.macbury.unamed.intefrace;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;

public class DeveloperConsole extends Interface implements KeyListener {
  public final static Color consoleColor = new Color(0,0,0,0.5f);
  private static final float CONSOLE_HEIGHT = 250.0f;
  private static final float LINE_HEIGHT = 24;
  private static final float CONSOLE_PADDING = 10;
  private String currentCommand = "";
  ArrayList<String> commandsLog;

  public DeveloperConsole() {
    commandsLog = new ArrayList<String>();
  }
  
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    gr.setColor(consoleColor);
    gr.fillRect(0, 0, gc.getWidth(), CONSOLE_HEIGHT);
    
    for (int i = 0; i < commandsLog.size(); i++) {
      int y = (int) ((int) (LINE_HEIGHT * i) + CONSOLE_PADDING);
      font.drawString(10, y, commandsLog.get(i));
    }
    
    font.drawString(10, CONSOLE_HEIGHT - LINE_HEIGHT - CONSOLE_PADDING, "> " + currentCommand + InterfaceManager.shared().getCursorText());
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {

  }

  @Override
  public void onEnter() {
    currentCommand = "";
    Input input = Core.instance().getContainer().getInput();
    input.addKeyListener(this);
  }

  @Override
  public void onExit() {
    Input input = Core.instance().getContainer().getInput();
    input.removeKeyListener(this);
  }

  @Override
  public void inputEnded() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void inputStarted() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isAcceptingInput() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public void setInput(Input arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyPressed(int code, char c) {
    Input input = Core.instance().getContainer().getInput();
    
    if (code == Input.KEY_ENTER) {
      commandsLog.add(currentCommand);
      if (commandsLog.size() > 8) {
        commandsLog.remove(0);
      }
      currentCommand = "";
    } else if (code == Input.KEY_BACK) {
      if (currentCommand.length() > 0) {
        currentCommand = currentCommand.substring(0, currentCommand.length()-1);
      }
    } else { 
      currentCommand += c;
    }
  }

  @Override
  public void keyReleased(int code, char c) {
  }

}
