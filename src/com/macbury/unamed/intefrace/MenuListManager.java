package com.macbury.unamed.intefrace;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;

public class MenuListManager extends ArrayList<MenuList> {
  
  private static final int MENU_MOVE_BUTTON_THROTTLE = 200;
  private static final int TEXT_PADDING = 15;
  private UnicodeFont font;
  private int x             = 0;
  private int y             = 0;
  private int menuPadding   = 30;
  int menuItemMoveTime      = 0;
  MenuList currentMenuList  = null;
  int currentItemIndex      = 0;
  private boolean startMoving;
  private MenuListManagerInterface menuListener;
  private MessageBox messageBox;
  
  public MenuListManager() throws SlickException {
    this.font          = Core.instance().getFont();
    this.messageBox    = new MessageBox(0,0,320,210);
  }
  
  public void render(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    if (currentMenuList != null) {
      gr.pushTransform();
      gr.translate(this.getX(), this.getY());
      
      messageBox.setHeight(getBoxHeight());
      messageBox.setWidth(getWidth());
      messageBox.draw(gr);
      
      int sy = 0;
      int x  = 20;
      
      gr.pushTransform();
      gr.translate(TEXT_PADDING, TEXT_PADDING);
      
      if (currentMenuList.haveTitle()) {
        InterfaceManager.shared().drawTextWithShadow(x, sy, currentMenuList.getTitle());
        sy = 40;
      }
      
      for (int i = 0; i < currentMenuList.size(); i++) {
        int y = i * this.menuPadding + sy;
        if (currentItemIndex == i) {
          InterfaceManager.shared().drawTextWithShadow(0, y, ">");
        }
        
        InterfaceManager.shared().drawTextWithShadow(x, y, currentMenuList.get(i).getName());
      }
      gr.popTransform();
      gr.popTransform(); 
    }
  }

  public float getWidth() {
    return 320;
  }

  public float getBoxHeight() {
    float height = currentMenuList.size() * this.menuPadding + 2*TEXT_PADDING;
    if (currentMenuList.haveTitle()) {
      height += 40;
    }
    return height;
  }

  public void pushList(MenuList list) {
    this.add(list);
    setCurrentMenuList(list);
  }

  private void setCurrentMenuList(MenuList list) {
    if (currentMenuList != list) {
      currentMenuList  = list;
      currentItemIndex = 0;
      menuItemMoveTime = 0;
      startMoving      = false;
    }
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
      } else if (input.isKeyPressed(Core.ACTION_KEY)) {
        SoundManager.shared().decision.playAsSoundEffect(1.0f, 1.0f, false);
        menuListener.onSelectItem(this.currentMenuList.get(currentItemIndex), currentMenuList);
        startMoving = false;
      } else if (input.isKeyPressed(Core.CANCEL_KEY)) {
        SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
        startMoving = false;
        popList();
      }
      
      if (startMoving) {
        SoundManager.shared().cursor.playAsSoundEffect(1.0f, 1.0f, false);
        menuListener.onItemChange(this.currentMenuList.get(currentItemIndex), currentMenuList);
      }
    }
  }

  public void popList() throws SlickException {
    if (this.size() > 1) {
      this.remove(this.size()-1); 
    }
    
    int last = this.size() - 1;
    if (last > 0) {
      setCurrentMenuList(this.get(last));
    } else {
      setCurrentMenuList(this.get(0));
      if (menuListener != null) {
        menuListener.onMenuExit();
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

  public void reset() {
    currentItemIndex = 0;
  }
}
