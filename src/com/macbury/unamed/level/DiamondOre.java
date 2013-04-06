package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

public class DiamondOre extends HarvestableBlock {

  public DiamondOre(int x, int y) {
    super(x, y);
    // TODO Auto-generated constructor stub
  }

  @Override
  public int getHardness() {
    return 100;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return null;
  }

}
