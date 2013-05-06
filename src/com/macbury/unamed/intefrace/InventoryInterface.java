package com.macbury.unamed.intefrace;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;

public class InventoryInterface extends Interface implements MenuListManagerInterface {

  public static final int TOGGLE_KEY = Input.KEY_C;
  private static final int WINDOW_PADDING = 10;
  private MenuListManager selectActionManager;
  private MenuList actionList;

  
  public InventoryInterface() throws SlickException {
    selectActionManager = new MenuListManager();
    selectActionManager.setMenuListener(this);
    actionList = new MenuList();
    
    actionList.add("Resources", 0);
    actionList.add("Items", 0);
    actionList.add("Equipment", 0);
    actionList.add("Crafting", 0);
    actionList.add("Map", 0);
    selectActionManager.pushList(actionList);
    
    selectActionManager.setX(WINDOW_PADDING);
    selectActionManager.setY(WINDOW_PADDING);
    selectActionManager.setWidth(Core.WINDOW_WIDTH - 2 * WINDOW_PADDING);
    selectActionManager.setVerticla(false);
    selectActionManager.setMinItemWidth(Core.WINDOW_WIDTH / actionList.size() - WINDOW_PADDING * 2);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    selectActionManager.render(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    Input input = gc.getInput();
    if (input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Core.CANCEL_KEY) || input.isKeyPressed(TOGGLE_KEY)) {
      this.close();
    }
    selectActionManager.update(gc, sb, delta);
  }

  @Override
  public void onEnter() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onExit() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean shouldBlockGamePlay() {
    return true;
  }

  @Override
  public boolean shouldRenderOnlyThis() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public void onItemChange(MenuItem item, MenuList currentMenuList) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onSelectItem(MenuItem item, MenuList currentMenuList)
      throws SlickException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onMenuExit() throws SlickException {
    // TODO Auto-generated method stub
    
  }

}
