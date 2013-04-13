package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;

public class DeveloperConsole extends Interface {
  public final static Color consoleColor = new Color(0,0,0,0.5f);
  private static final float CONSOLE_HEIGHT = 250.0f;
  private static final float LINE_HEIGHT = 20;
  private static final float CONSOLE_PADDING = 10;
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    gr.setColor(consoleColor);
    gr.fillRect(0, 0, gc.getWidth(), CONSOLE_HEIGHT);
    font.drawString(10, CONSOLE_HEIGHT - LINE_HEIGHT - CONSOLE_PADDING, "> " + InterfaceManager.shared().getCursorText());
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    // TODO Auto-generated method stub

  }

}
