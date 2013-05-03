package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public interface HUDComponentInterface {
  public void onHUDRender(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException;
}
