package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

abstract public class HarvestableBlock extends Block {
  
  public static final int HARDNESS_INFITNITY = 999999999;

  public HarvestableBlock(int x, int y) {
    super(x, y);
    this.solid = true;
  }
  
  abstract public int getHardness();

  abstract public InventoryItem harvestedByPlayer(Player byPlayer);
}
