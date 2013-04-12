package com.macbury.unamed.intefrace;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MenuList extends ArrayList<MenuItem> {
  private String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public boolean haveTitle() {
    return getTitle() != null;
  }

  public void add(String name, int id) {
    super.add(new MenuItem(name, id));
  }
}
