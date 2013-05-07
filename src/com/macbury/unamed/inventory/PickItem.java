package com.macbury.unamed.inventory;

public abstract class PickItem extends InventoryItem {

  public PickItem() {
    super();
    this.setItemType(InventoryItemType.Equipment);
  }
  
  @Override
  public String getKey() {
    return this.getClass().getSimpleName();
  }
  
}
