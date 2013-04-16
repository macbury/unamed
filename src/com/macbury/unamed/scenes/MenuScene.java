package com.macbury.unamed.scenes;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import com.macbury.procedular.WorldBuilder;
import com.macbury.unamed.Core;
import com.macbury.unamed.intefrace.MenuItem;
import com.macbury.unamed.intefrace.MenuList;
import com.macbury.unamed.intefrace.MenuListManager;
import com.macbury.unamed.intefrace.MenuListManagerInterface;

public class MenuScene extends BasicGameState implements MenuListManagerInterface {
  private static final int ITEM_INDEX_EXIT       = 3;
  private static final int ITEM_INDEX_START_GAME = 0;
  public  static final int STATE_MENU            = 4;
  private static final int MENU_ITEM_SIZE_NORMAL            = 0;
  private static final int MENU_ITEM_SIZE_BIG               = 1;
  private static final int MENU_ITEM_SIZE_CRASH_MY_COMPUTER = 2;
  private static final int ITEM_INDEX_LOAD_GAME = 1;
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
    
    File saveDirectory = Core.instance().getSaveDirectory(Core.DUNGON_FILE_NAME);
    
    if (saveDirectory.exists()) {
      mainMenuList.add("Continue", ITEM_INDEX_LOAD_GAME);
    }
    
    mainMenuList.add("New game", ITEM_INDEX_START_GAME);
    mainMenuList.add("Options", 3);
    mainMenuList.add("Exit", ITEM_INDEX_EXIT);
    
    selectGeneratedWorldSizeMenu = new MenuList();
    selectGeneratedWorldSizeMenu.setTitle("Select world size:");
    selectGeneratedWorldSizeMenu.add("Normal", MENU_ITEM_SIZE_NORMAL);
    selectGeneratedWorldSizeMenu.add("Big", MENU_ITEM_SIZE_BIG);
    selectGeneratedWorldSizeMenu.add("Crash my computer", MENU_ITEM_SIZE_CRASH_MY_COMPUTER);
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
    return STATE_MENU;
  }

  @Override
  public void onItemChange(MenuItem item, MenuList currentMenuList) {
    Log.info("Selected index: "+ item.getName());
  }

  @Override
  public void onSelectItem(MenuItem item, MenuList currentMenuList) {
    if (selectGeneratedWorldSizeMenu == currentMenuList) {
      switch (item.getId()) {
      case MENU_ITEM_SIZE_NORMAL:
        Core.instance().getGeneratingWorldState().setWorldSize(WorldBuilder.NORMAL);
      break;
      case MENU_ITEM_SIZE_BIG:
        Core.instance().getGeneratingWorldState().setWorldSize(WorldBuilder.BIG);
      break;
      case MENU_ITEM_SIZE_CRASH_MY_COMPUTER:
        Core.instance().getGeneratingWorldState().setWorldSize(WorldBuilder.CRASH_MY_COMPUTER);
      break;
      default:
        break;
      }
      Core.instance().enterState(GeneratingWorldState.STATE_GENERATIING, new FadeOutTransition(), new FadeInTransition());
    } else if (currentMenuList == mainMenuList) {
      if (item.getId() == ITEM_INDEX_START_GAME) {
        menuManager.pushList(selectGeneratedWorldSizeMenu);
      }
      if (item.getId() == ITEM_INDEX_LOAD_GAME) {
        Core.instance().enterState(GameplayScene.STATE_GAMEPLAY);
      }
      if (item.getId() == ITEM_INDEX_EXIT) {
        System.exit(0);
      }
    }
    Log.info("Action on index: "+ item.getName());
  }

}
