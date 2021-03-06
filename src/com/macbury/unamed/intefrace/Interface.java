package com.macbury.unamed.intefrace;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Interface {
  public abstract void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException;
  public abstract void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException;
  
  public abstract void onEnter();
  public abstract void onExit();
  
  public abstract boolean shouldBlockGamePlay();
  public abstract boolean shouldRenderOnlyThis();
  
  public void close() throws SlickException {
    InterfaceManager.shared().close(this);
  }
  
  public void push(Interface inte) throws SlickException {
    InterfaceManager.shared().push(inte);
  }
}
