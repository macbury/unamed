package com.macbury.unamed.intefrace;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.macbury.unamed.Core;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;

public class InventoryItemMenuItem extends MenuItem {
  InventoryItem item;
  public InventoryItemMenuItem(InventoryItem item) {
    super(item.getName(), 0);
    this.item = item;
  }

  @Override
  public String getName() {
    return item.getName();
  }

  @Override
  public void render(Graphics gr, int x, int y, int width, int height) throws SlickException {
    InventoryManager.shared().getImageForInventoryItem(item).drawCentered(x+3, y+11);
    super.render(gr, x+28, y, width, height);
    String quantityString = Integer.toString(item.getQuantity());
    int quantityWidth = Core.instance().getFont().getWidth(quantityString);
    InterfaceManager.shared().drawTextWithOutline(x + width - quantityWidth - 30, y, quantityString);
  }

  
}
