package com.macbury.unamed.inventory;

public abstract class PickItem extends InventoryItem {

  @Override
  public String getKey() {
    return this.getClass().getSimpleName();
  }
  
}
