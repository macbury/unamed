package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

public class GoldOre extends HarvestableBlock {

  public GoldOre(int x, int y) {
    super(x, y);
    // TODO Auto-generated constructor stub
  }

  @Override
  public int getHardness() {
    return 50;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return null;
  }

}
