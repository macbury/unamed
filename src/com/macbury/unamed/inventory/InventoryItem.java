package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.entity.CollectableItem;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;


public abstract class InventoryItem {
  public static final int STANDARD_HARVEST_POWER = 1;
  public static final int INFINITY_COUNT = -666;
  protected int elementCount = 0;

  public InventoryItem() {
    this.elementCount = 1;
  }
  
  public abstract String getKey();
  
  public abstract String getName();
  public abstract boolean place(Vector2f tilePos) throws SlickException;
  public abstract int harvestPower();
  public int getCount() {
    if (isInfinity()) {
      return 99;
    } else {
      return elementCount;
    }
  }
  
  public void addItem(int quantity) {
    if (!isInfinity()) {
      this.elementCount += quantity;
    }
  }
  
  public void setItemCount(int quantity) {
    if (!isInfinity()) {
      this.elementCount = quantity;
    }
  }
  
  public void popItem() {
    if (!isInfinity()) {
      this.elementCount--;
    }
  }
  
  public boolean isInfinity() {
    return elementCount == INFINITY_COUNT;
  }
  
  public boolean haveItems() {
    return elementCount > 0 || isInfinity() ;
  }

  public int getQuantity() {
    return elementCount;
  }


}
