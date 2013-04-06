package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.intefrace.MenuList;
import com.macbury.unamed.intefrace.MenuListManager;
import com.macbury.unamed.intefrace.MenuListManagerInterface;

public class MenuScene extends BasicGameState implements MenuListManagerInterface {
  private static final int ITEM_INDEX_EXIT       = 3;
  private static final int ITEM_INDEX_START_GAME = 0;
  public  static final int STATE_MENU            = 4;
  private MenuList mainMenuList;
  private MenuList selectGeneratedWorldSizeMenu;
  MenuListManager menuManager = null;
  private StateBasedGame sg;
  
  public MenuScene() throws SlickException {
    menuManager = new MenuListManager();
    menuManager.setX(60);
    menuManager.setY(30);
    menuManager.setMenuListener(this);
    
    mainMenuList = new MenuList();
    mainMenuList.add("Create new world");
    mainMenuList.add("Load world");
    mainMenuList.add("Options");
    mainMenuList.add("Exit");
    
    selectGeneratedWorldSizeMenu = new MenuList();
    selectGeneratedWorldSizeMenu.setTitle("Select world size:");
    selectGeneratedWorldSizeMenu.add("Normal");
    selectGeneratedWorldSizeMenu.add("Big");
    selectGeneratedWorldSizeMenu.add("Epic");
    selectGeneratedWorldSizeMenu.add("Crash my computer");
    menuManager.pushList(mainMenuList);
  }

  @Override
  public void init(GameContainer gc, StateBasedGame sg) throws SlickException {
    this.sg = sg;
    Log.info("Initializing Menu Scene");
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sg, Graphics gr) throws SlickException {
    menuManager.render(gc, sg, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sg, int delta) throws SlickException {
    menuManager.update(gc, sg, delta);
    
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return STATE_MENU;
  }

  @Override
  public void onItemChange(int index, MenuList currentMenuList) {
    Log.info("Selected index: "+ index);
  }

  @Override
  public void onSelectItem(int index, MenuList currentMenuList) {
    if (currentMenuList == mainMenuList) {
      if (index == ITEM_INDEX_START_GAME) {
        //Core.instance().enterState(LoadingState.STATE_GENERATIING);
        menuManager.pushList(selectGeneratedWorldSizeMenu);
      }
      if (index == 1) {
        Core.instance().enterState(GameplayScene.STATE_GAMEPLAY);
      }
      if (index == ITEM_INDEX_EXIT) {
        System.exit(0);
      }
    }
    Log.info("Action on index: "+ index);
  }

}
