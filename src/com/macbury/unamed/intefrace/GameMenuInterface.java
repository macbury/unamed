package com.macbury.unamed.intefrace;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.LevelLoader;
import com.macbury.unamed.scenes.MenuScene;

public class GameMenuInterface extends Interface implements MenuListManagerInterface {
  private static final int MENU_CONTINUE = 0;
  private static final int MENU_SAVE = 1;
  private static final int MENU_LOAD = 2;
  private static final int MENU_EXIT_TO_MAIN_MENU = 3;
  private static final int MENU_INVENTORY = 4;
  private MenuListManager menuManager;
  private MenuList mainMenuList;
  private MessageBox timerBox;

  public GameMenuInterface() throws SlickException {
    this.timerBox = new MessageBox(10, 10, 100, 50);
    menuManager = new MenuListManager();
    menuManager.setMenuListener(this);
    mainMenuList = new MenuList();
    
    mainMenuList.add("Inventory", MENU_INVENTORY);
    mainMenuList.add("Continue", MENU_CONTINUE);
    mainMenuList.add("Save", MENU_SAVE);
    
    //mainMenuList.add("Load", MENU_LOAD);
    mainMenuList.add("Exit to main menu", MENU_EXIT_TO_MAIN_MENU);
    menuManager.pushList(mainMenuList);
    
    menuManager.setX((int) (Core.WINDOW_WIDTH / 2 - menuManager.getWidth() / 2));
    menuManager.setY((int) (Core.WINDOW_HEIGHT / 2 - menuManager.getBoxHeight() / 2));
  }
  
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    menuManager.render(gc, sb, gr);
    
    this.timerBox.draw(gr);
    long s = Level.shared().gameplayTime;
    Core.instance().getFont().drawString(25, 25, String.format("%d:%02d:%02d", s/3600, (s%3600)/60, (s%60)));
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    menuManager.update(gc, sb, delta);
    
    if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
      this.close();
    }
  }

  @Override
  public void onEnter() {
    menuManager.reset();
  }

  @Override
  public void onExit() {
    
  }

  @Override
  public boolean shouldBlockGamePlay() {
    return true;
  }

  @Override
  public void onItemChange(MenuItem item, MenuList currentMenuList) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onSelectItem(MenuItem item, MenuList currentMenuList) throws SlickException {
    if (currentMenuList != mainMenuList) {
      return;
    }
    switch (item.getId()) {
      case MENU_INVENTORY:
        InterfaceManager.shared().push(new InventoryInterface());
      break;
      case MENU_SAVE:
        LevelLoader ll = new LevelLoader(Level.shared());
        ll.save();
        this.close();
      break;
      case MENU_CONTINUE:
        this.close();
      break;
      case MENU_EXIT_TO_MAIN_MENU:
        Core.instance().enterState(MenuScene.STATE_MENU, new FadeOutTransition(), new FadeInTransition());
      break;

      default:
        
      break;
    }
  }

  @Override
  public void onMenuExit() throws SlickException {
    this.close();
  }

  @Override
  public boolean shouldRenderOnlyThis() {
    return true;
  }

}
