package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

public class Cobblestone extends HarvestableBlock {

  public Cobblestone(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 20;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return null;
  }

}
