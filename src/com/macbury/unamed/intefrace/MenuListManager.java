package com.macbury.unamed.intefrace;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.util.FrameImageUtil;

public class MenuListManager extends ArrayList<MenuList> {
  private MenuListType menuType = MenuListType.Veritical;
  private static final int MENU_MOVE_BUTTON_THROTTLE = 200;
  private static final int TEXT_PADDING = 16;
  private static final int LINE_PADDING = 6;
  private static final int LINE_HEIGHT  = 38;
  private static final int TITLE_HEIGHT = 40;
  private static final float MAX_PULSE_ALPHA = 1.0f;
  private static final float MIN_PULSE_ALPHA = 0.4f;
  private static final float PULSE_SPEED = 0.002f;
  private int x             = 0;
  private int y             = 0;
  int menuItemMoveTime      = 0;
  MenuList currentMenuList  = null;
  int currentItemIndex      = 0;
  private boolean startMoving;
  private MenuListManagerInterface menuListener;
  private MessageBox messageBox;
  private Color pulseColor;
  private Color borderColor;
  private int pulseDirection = 1;
  
  private int width         = 320;
  private int minItemWidth  = 30;
  private int height = LINE_HEIGHT + TEXT_PADDING * 2;
  private int columns       = 3;
  private boolean autosize  = true;
  private boolean canSelect = true;
  
  public MenuListManager() throws SlickException {
    this.messageBox    = new MessageBox(0,0,320,210);
    pulseColor = new Color(255,255,255);
    pulseColor.a = MAX_PULSE_ALPHA;
    borderColor = new Color(255,255,255,0.9f);
  }
  
  public void render(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    if (currentMenuList != null) {
      gr.pushTransform();
      gr.translate(this.getX(), this.getY());
      
      if (menuType == MenuListType.Grid) {
        renderGrid(gc,sg,gr);
      } else if (isVerticla()) {
        renderVertical(gc,sg,gr);
      } else {
        renderHorizontal(gc,sg,gr);
      }
      
      gr.popTransform(); 
    }
  }

  private void renderGrid(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    messageBox.setHeight(getHeight());
    messageBox.setWidth(getWidth());
    messageBox.draw(gr);
    
    gr.pushTransform();
    gr.translate(TEXT_PADDING, TEXT_PADDING);
    
    int elementWidth = getWidth() / getColumns() - TEXT_PADDING;
    
    int lx = 0;
    int ly = 0;
    int lineCenterY = LINE_HEIGHT / 2 - Core.FONT_SIZE_CENTER_Y - 2;
    
    for (int i = 0; i < currentMenuList.size(); i++) {
      
      if (currentItemIndex == i) {
        Image cursorSkin = ImagesManager.shared().windowSpriteSheet.getSubImage(1, 0);
        FrameImageUtil.render(cursorSkin, gr, elementWidth, LINE_HEIGHT, lx, 0, pulseColor);
      }
      
      currentMenuList.get(i).render(gr, lx+LINE_PADDING, lineCenterY, elementWidth, LINE_HEIGHT);
      lx += elementWidth + LINE_PADDING;
    }
    
    gr.popTransform();
  }

  private void renderHorizontal(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    messageBox.setHeight(getHeight());
    messageBox.setWidth(getWidth());
    messageBox.draw(gr);
    
    gr.pushTransform();
    gr.translate(TEXT_PADDING, TEXT_PADDING);
    
    int x = 0;
    int lineCenterY = LINE_HEIGHT / 2 - Core.FONT_SIZE_CENTER_Y - 2;
    
    for (int i = 0; i < currentMenuList.size(); i++) {
      int textWidth = currentMenuList.get(i).getWidth() + LINE_PADDING * 2;
      textWidth = Math.max(getMinItemWidth(), textWidth);
      
      if (currentItemIndex == i) {
        Image cursorSkin = ImagesManager.shared().windowSpriteSheet.getSubImage(1, 0);
        FrameImageUtil.render(cursorSkin, gr, textWidth, LINE_HEIGHT, x, 0, pulseColor);
      }
      
      currentMenuList.get(i).render(gr, x+LINE_PADDING, lineCenterY, textWidth, LINE_HEIGHT);
      x += textWidth + LINE_PADDING;
    }
    
    gr.popTransform();
  }

  private void renderVertical(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    messageBox.setHeight(getBoxVerticalHeight());
    messageBox.setWidth(getWidth());
    messageBox.draw(gr);
    
    int sy = 0;
    
    gr.pushTransform();
    gr.translate(TEXT_PADDING, TEXT_PADDING);
    
    if (currentMenuList.haveTitle()) {
      InterfaceManager.shared().drawTextWithOutline(TEXT_PADDING, sy, currentMenuList.getTitle());
      sy = TITLE_HEIGHT;
    }
    
    int lineCenterY = LINE_HEIGHT / 2 - Core.FONT_SIZE_CENTER_Y - 2;
    int width       = getWidth() - TEXT_PADDING * 2;
    int y = sy;
    for (int i = 0; i < currentMenuList.size(); i++) {
      if (currentItemIndex == i) {
        Image cursorSkin = ImagesManager.shared().windowSpriteSheet.getSubImage(1, 0);
        FrameImageUtil.render(cursorSkin, gr, width, LINE_HEIGHT, 0, y, pulseColor);
      }
      
      currentMenuList.get(i).render(gr, TEXT_PADDING, y + lineCenterY, width, LINE_HEIGHT);
      
      y += LINE_HEIGHT + LINE_PADDING;
    }
    gr.popTransform();
  }


  public float getBoxVerticalHeight() {
    if (isAutosize()) {
      float height = currentMenuList.size() * LINE_HEIGHT + (2*TEXT_PADDING) + (2 * LINE_PADDING);
      if (currentMenuList.haveTitle()) {
        height += TITLE_HEIGHT;
      }
      return height;
    } else {
      return getHeight();
    }
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
    
    if (pulseColor.a > MAX_PULSE_ALPHA) {
      pulseDirection = -1;
    }
    
    if (pulseColor.a < MIN_PULSE_ALPHA) {
      pulseDirection = 1;
    }
    
    pulseColor.a += delta * pulseDirection * PULSE_SPEED;
    borderColor.a = pulseColor.a + 0.3f;
    if (startMoving) {
      menuItemMoveTime += delta;
    }
    
    if (menuItemMoveTime > MENU_MOVE_BUTTON_THROTTLE) {
      startMoving      = false;
      menuItemMoveTime = 0;
    }
    
    if (currentMenuList != null && !startMoving) {
      Input input = gc.getInput();
      
      int nextItem = Input.KEY_DOWN;
      int prevItem = Input.KEY_UP;
      
      if(getMenuType() == MenuListType.Horizontal) {
        nextItem = Input.KEY_RIGHT;
        prevItem = Input.KEY_LEFT;
      }
      
      
      if (input.isKeyDown(nextItem)) {
        if (currentItemIndex < currentMenuList.size() - 1) {
          currentItemIndex++;
          startMoving = true;
        }
      } else if (input.isKeyDown(prevItem)) {
        if (currentItemIndex > 0) {
          currentItemIndex--;
          startMoving = true;
        }
      } else if (input.isKeyPressed(Core.ACTION_KEY) && isCanSelect()) {
        SoundManager.shared().decision.playAsSoundEffect(1.0f, 1.0f, false);
        menuListener.onSelectItem(getCurrentSelectedMenuItem(), currentMenuList);
        startMoving = false;
      } else if (input.isKeyPressed(Core.CANCEL_KEY) && isCanSelect()) {
        SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
        startMoving = false;
        popList();
      }
      
      if (startMoving) {
        pulseColor.a   = 1.0f;
        pulseDirection = -1;
        SoundManager.shared().cursor.playAsSoundEffect(1.0f, 1.0f, false);
        menuListener.onItemChange(getCurrentSelectedMenuItem(), currentMenuList);
      }
    }
  }

  private MenuItem getCurrentSelectedMenuItem() {
    try{
      return this.currentMenuList.get(currentItemIndex);
    } catch (IndexOutOfBoundsException e) {
      return null;
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

  public boolean isVerticla() {
    return menuType == MenuListType.Veritical;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getMinItemWidth() {
    return minItemWidth;
  }

  public void setMinItemWidth(int minItemWidth) {
    this.minItemWidth = minItemWidth;
  }

  public boolean isCanSelect() {
    return canSelect;
  }

  public void setCanSelect(boolean canSelect) {
    this.canSelect = canSelect;
  }

  public boolean isAutosize() {
    return autosize;
  }

  public void setAutosize(boolean autosize) {
    this.autosize = autosize;
  }

  public MenuListType getMenuType() {
    return menuType;
  }

  public void setMenuType(MenuListType menuType) {
    this.menuType = menuType;
  }

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public void setList(MenuList resourceItems) {
    this.clear();
    this.pushList(resourceItems);
  }
}
