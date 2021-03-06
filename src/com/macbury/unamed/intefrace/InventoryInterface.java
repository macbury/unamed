package com.macbury.unamed.intefrace;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryItemType;
import com.macbury.unamed.inventory.InventoryManager;

public class InventoryInterface extends Interface implements MenuListManagerInterface {

  public static final int TOGGLE_KEY = Input.KEY_C;
  private static final int WINDOW_PADDING = 10;
  private static final int MENU_RESOURCE  = 1;
  private static final int MENU_ITEMS     = 2;
  private static final int MENU_EQUIPMENT = 3;
  private static final int MENU_CRAFTING  = 4;
  private static final int MENU_MAP       = 5;
  private static final int HOTBAR_HEIGHT  = 80;
  private static final int MENU_WEAPONS = 0;
  private MenuListManager selectActionManager;
  private MenuList actionList;
  private MenuListManager itemListManager;
  
  int currentMenu = MENU_RESOURCE;
  private MessageBox hotbarWindow;
  
  public InventoryInterface() throws SlickException {
    selectActionManager = new MenuListManager();
    selectActionManager.setMenuListener(this);
    selectActionManager.setCanSelect(false);
    actionList = new MenuList();
    
    actionList.add("Resources", MENU_RESOURCE);
    actionList.add("Items", MENU_ITEMS);
    actionList.add("Equipment", MENU_EQUIPMENT);
    actionList.add("Weapons", MENU_WEAPONS);
    actionList.add("Crafting", MENU_CRAFTING);
    actionList.add("Map", MENU_MAP);
    
    selectActionManager.pushList(actionList);
    
    selectActionManager.setX(WINDOW_PADDING);
    selectActionManager.setY(WINDOW_PADDING);
    selectActionManager.setWidth(Core.WINDOW_WIDTH - 2 * WINDOW_PADDING);
    selectActionManager.setMenuType(MenuListType.Horizontal);
    selectActionManager.setMinItemWidth(Core.WINDOW_WIDTH / actionList.size() - WINDOW_PADDING * 2);
    
    itemListManager = new MenuListManager();
    itemListManager.setMenuListener(this);
    
    itemListManager.setMenuType(MenuListType.Veritical);
    itemListManager.setAutosize(false);
    itemListManager.setX(WINDOW_PADDING);
    itemListManager.setY(selectActionManager.getHeight() + selectActionManager.getY() + WINDOW_PADDING);
    
    int itemsWidth = Core.WINDOW_WIDTH - 2 * WINDOW_PADDING;
    
    itemListManager.setWidth(itemsWidth);
    itemListManager.setHeight(Core.WINDOW_HEIGHT - itemListManager.getY() - WINDOW_PADDING * 2 - HOTBAR_HEIGHT);
    
    hotbarWindow = new MessageBox(WINDOW_PADDING, itemListManager.getY() + itemListManager.getHeight() + WINDOW_PADDING, Core.WINDOW_WIDTH - 2 * WINDOW_PADDING, HOTBAR_HEIGHT);
    updateActionScreen();
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    selectActionManager.render(gc, sb, gr);
    hotbarWindow.draw(gr);
    //if (currentMenu == MENU_RESOURCE) {
      itemListManager.render(gc, sb, gr);
  //  }
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    Input input = gc.getInput();
    if (input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Core.CANCEL_KEY) || input.isKeyPressed(TOGGLE_KEY)) {
      this.close();
    }
    
    //if (currentMenu == MENU_RESOURCE) {
      itemListManager.update(gc, sb, delta);
   // }
    
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
  public void onItemChange(MenuItem item, MenuList currentMenuList) throws SlickException {
    if (currentMenuList == actionList) {
      currentMenu = item.getId();
      
      updateActionScreen();
    }
  }

  private void updateActionScreen() throws SlickException {
    if (currentMenu == MENU_RESOURCE) {
      itemListManager.setList(InventoryManager.shared().getItemTypes(InventoryItemType.Resource));
    }
    if (currentMenu == MENU_ITEMS) {
      itemListManager.setList(InventoryManager.shared().getItemTypes(InventoryItemType.Item));
    }
    
    if (currentMenu == MENU_WEAPONS) {
      itemListManager.setList(InventoryManager.shared().getItemTypes(InventoryItemType.Weapon));
    }
    
    if (currentMenu == MENU_EQUIPMENT) {
      itemListManager.setList(InventoryManager.shared().getItemTypes(InventoryItemType.Equipment));
    }
  }

  @Override
  public void onSelectItem(MenuItem item, MenuList currentMenuList) throws SlickException {
    if (InventoryItemMenuItem.class.isInstance(item)) {
      InventoryItemMenuItem menuItem = (InventoryItemMenuItem) item;
      InventoryItem inventoryItem    = menuItem.getItem();
      InventoryManager.shared().setItemByItsType(inventoryItem);
      Core.log(this.getClass(), "Setting item: " +item.getName());
    }
  }

  @Override
  public void onMenuExit() throws SlickException {
    // TODO Auto-generated method stub
    
  }

}
