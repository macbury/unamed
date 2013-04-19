package com.macbury.unamed.intefrace;

import org.newdawn.slick.SlickException;

public interface MenuListManagerInterface {
  void onItemChange(MenuItem item, MenuList currentMenuList);
  void onSelectItem(MenuItem item, MenuList currentMenuList) throws SlickException;
}
