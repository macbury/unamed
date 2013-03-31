package com.macbury.unamed.entity;

import com.macbury.unamed.inventory.InventoryItem;

public abstract class BlockEntity extends Entity {
  protected int hardness = 1;
  public abstract boolean use();
  public abstract InventoryItem harvest(int power, Player byPlayer);
}
