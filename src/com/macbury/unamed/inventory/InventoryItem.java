package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;


public abstract class InventoryItem {
  public static final int INFINITY_COUNT = -1;
  protected int elementCount = 0;
  protected Player owner;
  
  public InventoryItem(Player entity) {
    this.owner = entity;
  }
  
  public abstract String getName();
  public abstract void use() throws SlickException;
}
