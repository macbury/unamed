package com.macbury.unamed.intefrace.developerconsole;

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
import com.macbury.unamed.intefrace.Interface;
import com.macbury.unamed.intefrace.InterfaceManager;

public class DeveloperConsole extends Interface implements KeyListener {
  public final static Color consoleColor = new Color(0,0,0,0.7f);
  private static final float CONSOLE_HEIGHT = 320.0f;
  private static final float LINE_HEIGHT = 24;
  private static final float CONSOLE_PADDING = 10;
  private String currentCommand = "";
  ArrayList<String> commandsLog;
  ArrayList<String> executedCommands;
  public ArrayList<ConsoleCommand> commands;
  public DeveloperConsole() {
    commandsLog      = new ArrayList<String>();
    executedCommands = new ArrayList<String>(); 
    commands         = new ArrayList<ConsoleCommand>();
    registerCommand(HelpCommand.class);
    registerCommand(ClearCommand.class);
    registerCommand(GiveCommand.class);
    registerCommand(MoveRandomCommand.class);
    registerCommand(DamageCommand.class);
    registerCommand(SpawnNpcCommand.class);
    registerCommand(MessageCommand.class);
    registerCommand(DumpLevelCommand.class);
    registerCommand(SetCommand.class);
  }
  
  public void registerCommand(Class<? extends ConsoleCommand> klass) {
    ConsoleCommand command = null;
    try {
      command = klass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    command.setConsole(this);
    commands.add(command);
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
    
    switch (code) {
      case Input.KEY_ENTER:
        parseCommand();
      break;
      
      case Input.KEY_UP:
        if (commandsLog.size() > 0) {
          currentCommand = executedCommands.get(executedCommands.size() - 1);
        }
      break;
      
      case Input.KEY_SPACE:
        currentCommand += " ";
      break;
      
      case Input.KEY_BACK:
        if (currentCommand.length() > 0) {
          currentCommand = currentCommand.substring(0, currentCommand.length()-1);
        }
      break;
      
      default:
        if (Character.isLetter(c) || Character.isDigit(c) || c == '\'') {
          currentCommand += c;
        }

      break;
    }
  }
  
  public void print(String string) {
    commandsLog.add(string);
    if (commandsLog.size() > CONSOLE_HEIGHT / LINE_HEIGHT  - 2) {
      commandsLog.remove(0);
    }
  }

  private void parseCommand() {
    currentCommand = currentCommand.trim();
    
    boolean foundCommand = false;
    for (ConsoleCommand command : this.commands) {
      if (command.parseCommand(currentCommand)) {
        foundCommand = true;
        break;
      }
    }
    if (!foundCommand) {
      print("Undefined command: " + currentCommand + " type 'help' for more commands");
    }
    executedCommands.add(currentCommand);
    currentCommand = "";
  }

  @Override
  public void keyReleased(int code, char c) {
  }

  @Override
  public boolean shouldBlockGamePlay() {
    return true;
  }

  @Override
  public boolean shouldRenderOnlyThis() {
    // TODO Auto-generated method stub
    return false;
  }


}
