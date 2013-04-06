package com.macbury.unamed.intefrace;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;

public class MenuListManager extends ArrayList<MenuList> {
  
  private static final int MENU_MOVE_BUTTON_THROTTLE = 200;
  private UnicodeFont font;
  private int x             = 0;
  private int y             = 0;
  private int menuPadding   = 30;
  int menuItemMoveTime      = 0;
  MenuList currentMenuList  = null;
  int currentItemIndex      = 0;
  private boolean startMoving;
  private MenuListManagerInterface menuListener;
  
  public MenuListManager() throws SlickException {
    this.font = Core.instance().getFont();
  }
  
  public void render(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    if (currentMenuList != null) {
      gr.pushTransform();
      gr.translate(this.getX(), this.getY());
      
      for (int i = 0; i < currentMenuList.size(); i++) {
        int x = 20;
        int y = i * this.menuPadding;
        if (currentItemIndex == i) {
          font.drawString(0, y, ">");
        }
        
        font.drawString(x, y, currentMenuList.get(i));
      }
      
      gr.popTransform(); 
    }
  }

  public void pushList(MenuList list) {
    this.add(list);
    setCurrentMenuList(list);
  }

  private void setCurrentMenuList(MenuList list) {
    currentMenuList  = list;
    currentItemIndex = 0;
    menuItemMoveTime = 0;
    startMoving      = false;
  }

  public void update(GameContainer gc, StateBasedGame sg, int delta) throws SlickException {
    
    if (startMoving) {
      menuItemMoveTime += delta;
    }
    
    if (menuItemMoveTime > MENU_MOVE_BUTTON_THROTTLE) {
      startMoving      = false;
      menuItemMoveTime = 0;
    }
    
    if (currentMenuList != null && !startMoving) {
      Input input = gc.getInput();
      
      if (input.isKeyDown(Input.KEY_DOWN)) {
        if (currentItemIndex < currentMenuList.size() - 1) {
          currentItemIndex++;
          startMoving = true;
        }
      } else if (input.isKeyDown(Input.KEY_UP)) {
        if (currentItemIndex > 0) {
          currentItemIndex--;
          startMoving = true;
        }
      } else if (input.isKeyDown(Core.ACTION_KEY)) {
        menuListener.onSelectItem(currentItemIndex, currentMenuList);
        startMoving = true;
      }
      
      if (startMoving) {
        menuListener.onItemChange(currentItemIndex, currentMenuList);
      }
    }
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setMenuListener(MenuListManagerInterface menuListener) {
    this.menuListener = menuListener;
  }
}
