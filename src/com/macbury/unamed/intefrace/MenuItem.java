package com.macbury.unamed.intefrace;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.macbury.unamed.Core;

public class MenuItem {
  String name;
  int    id;
  
  public MenuItem(String name, int id) {
    this.name = name;
    this.id   = id;
  }
  
  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

  public void render(Graphics gr, int x, int y, int width, int height) throws SlickException {
    InterfaceManager.shared().drawTextWithOutline(x, y, getName());
  }

  public int getWidth() throws SlickException {
    return Core.instance().getFont().getWidth(getName());
  }
}
